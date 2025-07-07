package com.yazikochesalna.messagingservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadMessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.PayloadNotificationDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseResultType;
import com.yazikochesalna.messagingservice.exception.UserNotHaveAccessToChatCustomException;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
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
public class WebSocketServiceImpl implements WebSocketService {

    private final Map<Long, Set<WebSocketSession>> activeSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatServiceClient chatServiceClient;
    private final KafkaProducerService sendMessageService;


    private final int SEND_TIME_LIMIT = 10 * 1000;
    private final int SEND_BUFFER_SIZE_LIMIT = 512 * 1024;


    @Override
    public void addSession(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        var concurrentSession = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT);
        Set<WebSocketSession> sessions = activeSessions.get(userId);
        if (sessions == null) {
            sessions = new CopyOnWriteArraySet<>();
            sessions.add(concurrentSession);
            activeSessions.put(userId, sessions);
        } else {
            sessions.add(concurrentSession);
        }
    }

    @Override
    public void removeSession(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        Set<WebSocketSession> sessions = activeSessions.get(userId);
        if (sessions != null) {
            Set<WebSocketSession> updatedSessions = sessions.stream()
                    .filter(decoratedSession ->
                            !((ConcurrentWebSocketSessionDecorator) decoratedSession).getDelegate().equals(session))
                    .collect(Collectors.toSet());
            if (updatedSessions.isEmpty()) {
                activeSessions.remove(userId);
            }
        }
    }


    @Override
    public void sendMessage(WebSocketSession session, MessageDTO messageDTO) {
        var userId = (Long) session.getAttributes().get("userId");

        PayloadMessageDTO payload = messageDTO.<PayloadMessageDTO>getPayload();
        if (!chatServiceClient.isUserInChat(userId, payload.getChatId())) {
            throw new UserNotHaveAccessToChatCustomException();
        }

        var message = messageDTO.setPayload(payload);
        sendMessageToKafka(session, message);
    }

    private void sendMessageToKafka(WebSocketSession session, MessageDTO messageDTO) {
        BiConsumer<SendResult<String, MessageDTO>, Throwable> callback = (result, throwable) ->{
            if(throwable != null){
                sendOKResponse(session, messageDTO.getRequestId(), messageDTO.getMessageId());
            }else {
                sendErrorResponse(session, ResponseResultType.NOT_SENT_TO_STORAGE, messageDTO.getRequestId());
            }
        };
        sendMessageService.sendMessage(messageDTO, callback);
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        sendMessageService.sendMessage(messageDTO);
    }

    @Override
    public void sendErrorResponse(WebSocketSession session, ResponseResultType responseResultType, Long requestId) {
        var responseDTO = ResponseDTO.builder()
                .result(responseResultType)
                .requestId(requestId)
                .build();
        sendResponseToWebSocketSession(session, responseDTO);
    }
    private void sendOKResponse(WebSocketSession session, Long requestId, UUID messageId) {
        var responseDTO = ResponseDTO.builder()
                .result(ResponseResultType.OK)
                .requestId(requestId)
                .messageId(messageId)
                .build();
        sendResponseToWebSocketSession(session, responseDTO);

    }

    private void sendResponseToWebSocketSession(WebSocketSession session, ResponseDTO responseDTO) {
        try {
            if(isOpenSession(session)){
                var jsonResponse = objectMapper.writeValueAsString(responseDTO);
                sendJsonToWebSocketSession(session, jsonResponse);
            }
        } catch (Exception ignored) {

        }
    }


    @Override
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
            Set<WebSocketSession> recipientSessions = activeSessions.get(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (WebSocketSession recipientSession : recipientSessions) {
                    setMessageToWebSocketSession(recipientSession, messageDTO);
                }
            }
        }
    }

    private void setMessageToWebSocketSession(WebSocketSession session, MessageDTO messageDTO) {
        try {
            if(isOpenSession(session)){
                var jsonResponse = objectMapper.writeValueAsString(messageDTO);
                sendJsonToWebSocketSession(session, jsonResponse);
            }
        } catch (Exception ignored) {

        }
    }


    private void sendJsonToWebSocketSession(WebSocketSession session, String jsonMessage) {

        try {
                session.sendMessage(new TextMessage(jsonMessage));

        } catch (Exception e) {
            System.err.println("Ошибка отправки сообщения по ws, закрытие соединения: " + e.getMessage());
            try {
                if (!isOpenSession(session)) {
                    session.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static boolean isOpenSession(WebSocketSession session) {
        return session.isOpen();
    }


}
