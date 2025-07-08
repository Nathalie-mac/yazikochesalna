package com.yazikochesalna.messagingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.*;
import com.yazikochesalna.messagingservice.dto.response.ResponseDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final Map<Long, Set<ConcurrentWebSocketSessionDecorator>> activeSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatServiceClient chatServiceClient;
    private final KafkaProducerService kafkaProducerService;


    private final int SEND_TIME_LIMIT = 10 * 1000;
    private final int SEND_BUFFER_SIZE_LIMIT = 512 * 1024;

    private static boolean isOpenSession(WebSocketSession session) {
        return session.isOpen();
    }

    public void addSession(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        var concurrentSession = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT);
        Set<ConcurrentWebSocketSessionDecorator> sessions = activeSessions.get(userId);
        if (sessions == null) {
            sessions = new CopyOnWriteArraySet<>();
            sessions.add(concurrentSession);
            activeSessions.put(userId, sessions);
        } else {
            sessions.add(concurrentSession);
        }
    }

    public void removeSession(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        Set<ConcurrentWebSocketSessionDecorator> sessions = activeSessions.get(userId);
        if (sessions != null) {
            Set<WebSocketSession> updatedSessions = sessions.stream()
                    .filter(decoratedSession ->
                            !decoratedSession.getDelegate().equals(session))
                    .collect(Collectors.toSet());
            if (updatedSessions.isEmpty()) {
                activeSessions.remove(userId);
            }
        }
    }

    public void sendMessage(WebSocketSession session, AwaitingResponseMessageDTO awaitingResponseMessageDTO) {
        var userId = (Long) session.getAttributes().get("userId");

        PayloadMessageDTO payload = awaitingResponseMessageDTO.<PayloadMessageDTO>getPayload();
        if (!chatServiceClient.isUserInChat(userId, payload.getChatId())) {
            sendErrorResponse(session, ResponseResultType.NOT_ALLOWED, awaitingResponseMessageDTO.getRequestId());
            return;
        }
        payload.setSenderId(userId);
        var message = awaitingResponseMessageDTO.setPayload(payload);
        sendMessageToKafka(session, message);

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
        ConcurrentWebSocketSessionDecorator concurrentSession = getConcurrentWebSocketSessionDecorator(session);
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
        ConcurrentWebSocketSessionDecorator concurrentSession = getConcurrentWebSocketSessionDecorator(session);
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

    private ConcurrentWebSocketSessionDecorator getConcurrentWebSocketSessionDecorator(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        Set<ConcurrentWebSocketSessionDecorator> userSessions = activeSessions.get(userId);
        if (userSessions != null) {
            return userSessions.stream()
                    .filter(decoratedSession ->
                            decoratedSession.getDelegate().equals(session))
                    .findAny().get();
        }
        return null;

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
        Long chatId;
        if (messageDTO.getType() == MessageType.MESSAGE) {
            PayloadMessageDTO payload = messageDTO.<PayloadMessageDTO>getPayload();
            chatId = payload.getChatId();
        } else {
            PayloadNotificationDTO payload = messageDTO.<PayloadNotificationDTO>getPayload();
            chatId = payload.getChatId();
        }

        sendMessageToActiveSessions(messageDTO, chatId);

    }

    private void sendMessageToActiveSessions(MessageDTO messageDTO, Long chatId) {
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(chatId);

        for (Long recipientId : recipientUsers) {
            Set<ConcurrentWebSocketSessionDecorator> recipientSessions = activeSessions.get(recipientId);
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
