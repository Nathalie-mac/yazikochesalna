package com.yazikochesalna.messagingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.*;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
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
                    .filter(decoratedSession -> !((ConcurrentWebSocketSessionDecorator) decoratedSession).getDelegate().equals(session))
                    .collect(Collectors.toSet());
            if (updatedSessions.isEmpty()) {
                activeSessions.remove(userId);
            }
        }

    }


    @Override
    public void sendMessageToKafka(WebSocketSession session, MessageDTO messageDTO) {
        var userId = (Long) session.getAttributes().get("userId");
        PayloadDTO payload;
        if (messageDTO.getType().equals(MessageType.MESSAGE)) {
            payload = messageDTO.<PayloadMessageDTO>getPayload().setSenderId(userId);
        } else {
            payload = messageDTO.<PayloadNotificationDTO>getPayload();
        }

        var message = messageDTO.setPayload(payload);


        sendMessageService.sendMessage(message);
    }


    @Override
    public void sendNotificationToKafka(MessageDTO messageDTO) {
        sendMessageService.sendMessage(messageDTO);
    }


    @Override
    public void broadcastMessageToParticipants(MessageDTO messageDTO) {
        Long chatId;
        if (messageDTO.getType() == MessageType.MESSAGE) {
            PayloadMessageDTO payload = messageDTO.<PayloadMessageDTO>getPayload();
            Long senderId = payload.getSenderId();
            chatId = payload.getChatId();
            if (!chatServiceClient.isUserInChat(senderId, chatId)) {
                return;
            }
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
                    sendMessage(recipientSession, messageDTO);
                }
            }
        }
    }


    private void sendMessage(WebSocketSession session, MessageDTO receiveMessageDTO) {

        try {
            if (session.isOpen()) {
                String jsonResponse = objectMapper.writeValueAsString(receiveMessageDTO);
                //вот би (била егэ) сделать неблокирующее отправление по вебсокетам
                session.sendMessage(new TextMessage(jsonResponse));
            }

        } catch (IOException e) {
            System.err.println("Ошибка отправки сообщения по ws, закрытие соединения: " + e.getMessage());

            try {
                if (!session.isOpen()) {
                    session.close();
                }
            } catch (IOException ignored) {
            }
        }
    }


}
