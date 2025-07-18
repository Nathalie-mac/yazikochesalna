package com.yazikochesalna.messagingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.events.AwaitingResponseEventDTO;
import com.yazikochesalna.messagingservice.dto.events.EventDTO;
import com.yazikochesalna.messagingservice.dto.events.EventType;
import com.yazikochesalna.messagingservice.dto.events.payload.PayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.ChatPayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatMemberUpdatePayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatMessagePayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.chat.impl.ChatPinnedMessagePayloadDTO;
import com.yazikochesalna.messagingservice.dto.events.payload.user.UserPayloadDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class WebSocketEventService {

    private final ObjectMapper objectMapper;
    private final ChatServiceClient chatServiceClient;
    private final KafkaProducerService kafkaProducerService;
    private final WebSocketSessionService webSocketSessionService;


    private static boolean isOpenSession(WebSocketSession session) {
        return session.isOpen();
    }

    private static void setUserIdInDTO(AwaitingResponseEventDTO awaitingResponseEventDTO, Long userId) {
        awaitingResponseEventDTO.getPayload();
        PayloadDTO payload = switch (awaitingResponseEventDTO.getType()) {
            case MESSAGE -> awaitingResponseEventDTO.<ChatMessagePayloadDTO>getPayload().setSenderId(userId);
            case PIN -> awaitingResponseEventDTO.<ChatPinnedMessagePayloadDTO>getPayload().setMemberId(userId);
            default -> awaitingResponseEventDTO.getPayload();
        };
        awaitingResponseEventDTO.setPayload(payload);
    }

    public void sendMessage(WebSocketSession session, AwaitingResponseEventDTO awaitingResponseEventDTO) {
        var userId = webSocketSessionService.getUserId(session);

        Long chatId = getChatId(awaitingResponseEventDTO);
        if (!chatServiceClient.isUserInChat(userId, chatId)) {
            sendErrorResponse(session, ResponseResultType.NOT_ALLOWED, awaitingResponseEventDTO.getRequestId());
            return;
        }

        setUserIdInDTO(awaitingResponseEventDTO, userId);

        sendMessageToKafka(session, awaitingResponseEventDTO);

    }

    private Long getChatId(EventDTO eventDTO) {
        return switch (eventDTO.getType()) {
            case MESSAGE, NEW_CHAT_AVATAR, NEW_MEMBER, DROP_MEMBER, PIN ->
                    eventDTO.<ChatPayloadDTO>getPayload().getChatId();
            default -> null;
        };
    }

    private void sendMessageToKafka(WebSocketSession session, AwaitingResponseEventDTO awaitingResponseEventDTO) {
        BiConsumer<SendResult<String, EventDTO>, Throwable> callback = (result, throwable) -> {
            if (awaitingResponseEventDTO.getRequestId() != null) {
                if (throwable == null) {
                    sendOKResponse(session, awaitingResponseEventDTO.getRequestId(), awaitingResponseEventDTO.getMessageId());
                } else {
                    sendErrorResponse(session, ResponseResultType.KAFKA_PROBLEM, awaitingResponseEventDTO.getRequestId());
                }
            }
        };

        kafkaProducerService.sendMessage(awaitingResponseEventDTO, callback);
    }

    public void sendMessage(EventDTO eventDTO) {
        kafkaProducerService.sendMessage(eventDTO);
    }
    public void sendEvent(EventDTO eventDTO) {
        kafkaProducerService.sendEvent(eventDTO);
    }

    public void sendErrorResponse(WebSocketSession session, ResponseResultType responseResultType, Long requestId) {
        ConcurrentWebSocketSessionDecorator concurrentSession = webSocketSessionService.getConcurrentSession(session);
        if (concurrentSession == null) {
            return;
        }
        var responseDTO = ResponseDTO.builder()
                .result(responseResultType)
                .requestId(requestId)
                .build();
        sendResponseToWebSocketSession(concurrentSession, responseDTO);
    }

    private void sendOKResponse(WebSocketSession session, Long requestId, UUID messageId) {
        ConcurrentWebSocketSessionDecorator concurrentSession = webSocketSessionService.getConcurrentSession(session);
        if (concurrentSession == null) {
            return;
        }
        var responseDTO = ResponseDTO.builder()
                .result(ResponseResultType.OK)
                .requestId(requestId)
                .messageId(messageId)
                .build();
        sendResponseToWebSocketSession(concurrentSession, responseDTO);

    }


    private void sendResponseToWebSocketSession(ConcurrentWebSocketSessionDecorator session, ResponseDTO responseDTO) {
        try {
            if (isOpenSession(session)) {
                var jsonResponse = objectMapper.writeValueAsString(responseDTO);
                sendJsonToWebSocketSession(session, jsonResponse);
            }
        } catch (Exception ignored) {

        }
    }

    public void broadcastEventToParticipants(EventDTO eventDTO) {
        Long userId = getUserId(eventDTO);
        List<Long> recipientUsers = chatServiceClient.getUserCompanionsByUserId(userId);
        sendEventToActiveSessions(eventDTO, recipientUsers);

    }

    private Long getUserId(EventDTO eventDTO) {
        return switch (eventDTO.getType()) {
            case NEW_USER_AVATAR, NEW_USERNAME -> eventDTO.<UserPayloadDTO>getPayload().getUserId();
            default -> null;
        };
    }

    public void broadcastMessageToParticipants(EventDTO eventDTO) {
        Long chatId = getChatId(eventDTO);
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(chatId);
        if (eventDTO.getType() == EventType.DROP_MEMBER) {
            recipientUsers.add(eventDTO.<ChatMemberUpdatePayloadDTO>getPayload().getMemberId());
        }

        sendEventToActiveSessions(eventDTO, recipientUsers);

    }

    private void sendEventToActiveSessions(EventDTO eventDTO, List<Long> recipientUsers) {
        for (Long recipientId : recipientUsers) {
            Set<ConcurrentWebSocketSessionDecorator> recipientSessions =
                    webSocketSessionService.getUserSessions(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (ConcurrentWebSocketSessionDecorator recipientSession : recipientSessions) {
                    sendEventToWebSocketSession(recipientSession, eventDTO);
                }
            }
        }
    }

    private void sendEventToWebSocketSession(ConcurrentWebSocketSessionDecorator session, EventDTO eventDTO) {
        try {
            if (isOpenSession(session)) {
                var jsonResponse = objectMapper.writeValueAsString(eventDTO);
                sendJsonToWebSocketSession(session, jsonResponse);
            }
        } catch (Exception ignored) {

        }
    }

    private void sendJsonToWebSocketSession(ConcurrentWebSocketSessionDecorator session, String jsonMessage) {

        try {
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (Exception e) {
            System.err.println("Ошибка отправки сообщения по ws: " + e.getMessage());
        }
    }


}
