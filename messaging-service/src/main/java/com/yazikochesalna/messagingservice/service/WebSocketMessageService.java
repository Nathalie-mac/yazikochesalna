package com.yazikochesalna.messagingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadNotificationPinDTO;
import com.yazikochesalna.messagingservice.dto.request.AwaitingResponseMessageDTO;
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
public class WebSocketMessageService {

    private final ObjectMapper objectMapper;
    private final ChatServiceClient chatServiceClient;
    private final KafkaProducerService kafkaProducerService;
    private final WebSocketSessionService webSocketSessionService;


    private static boolean isOpenSession(WebSocketSession session) {
        return session.isOpen();
    }


    public void sendMessage(WebSocketSession session, AwaitingResponseMessageDTO awaitingResponseMessageDTO) {
        var userId = webSocketSessionService.getUserId(session);

        Long chatId = awaitingResponseMessageDTO.getPayload().getChatId();
        if (!chatServiceClient.isUserInChat(userId, chatId)) {
            sendErrorResponse(session, ResponseResultType.NOT_ALLOWED, awaitingResponseMessageDTO.getRequestId());
            return;
        }

        if (awaitingResponseMessageDTO.getType() == MessageType.MESSAGE) {
            PayloadMessageDTO payload = awaitingResponseMessageDTO.<PayloadMessageDTO>getPayload();
            payload.setSenderId(userId);
            awaitingResponseMessageDTO.setPayload(payload);
        } else if (awaitingResponseMessageDTO.getType() == MessageType.PIN) {
            PayloadNotificationPinDTO payload = awaitingResponseMessageDTO.<PayloadNotificationPinDTO>getPayload();
            payload.setMemberId(userId);
            awaitingResponseMessageDTO.setPayload(payload);
        }

        sendMessageToKafka(session, awaitingResponseMessageDTO);

    }

    private void sendMessageToKafka(WebSocketSession session, AwaitingResponseMessageDTO awaitingResponseMessageDTO) {
        BiConsumer<SendResult<String, MessageDTO>, Throwable> callback = (result, throwable) -> {
            if (awaitingResponseMessageDTO.getRequestId() != null) {
                if (throwable == null) {
                    sendOKResponse(session, awaitingResponseMessageDTO.getRequestId(), awaitingResponseMessageDTO.getMessageId());
                } else {
                    sendErrorResponse(session, ResponseResultType.KAFKA_PROBLEM, awaitingResponseMessageDTO.getRequestId());
                }
            }
        };

        kafkaProducerService.sendMessage(new MessageDTO(awaitingResponseMessageDTO), callback);
    }

    public void sendMessage(MessageDTO messageDTO) {
        kafkaProducerService.sendMessage(messageDTO);
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

    public void broadcastMessageToParticipants(MessageDTO messageDTO) {
        Long chatId = messageDTO.getPayload().getChatId();

        sendMessageToActiveSessions(messageDTO, chatId);

    }

    private void sendMessageToActiveSessions(MessageDTO messageDTO, Long chatId) {
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(chatId);

        for (Long recipientId : recipientUsers) {
            Set<ConcurrentWebSocketSessionDecorator> recipientSessions =
                    webSocketSessionService.getUserSessions(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (ConcurrentWebSocketSessionDecorator recipientSession : recipientSessions) {
                    setMessageToWebSocketSession(recipientSession, messageDTO);
                }
            }
        }
    }

    private void setMessageToWebSocketSession(ConcurrentWebSocketSessionDecorator session, MessageDTO messageDTO) {
        try {
            if (isOpenSession(session)) {
                var jsonResponse = objectMapper.writeValueAsString(messageDTO);
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
