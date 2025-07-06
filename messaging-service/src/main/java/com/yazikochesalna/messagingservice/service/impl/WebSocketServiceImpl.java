package com.yazikochesalna.messagingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.mapper.MessageDTOMapper;
import com.yazikochesalna.messagingservice.dto.mapper.SendRequestMessageDTOMapper;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.exception.ReceiveMessageCustomException;
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
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private static final AtomicLong requestIdCounter = new AtomicLong(0);
    private final Map<Long, Set<WebSocketSession>> activeSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatServiceClient chatServiceClient;
    private final KafkaProducerService sendMessageService;
    private final SendRequestMessageDTOMapper sendRequestMessageDTOMapper;
    private final MessageDTOMapper messageDTOMapper;

    private final int SEND_TIME_LIMIT = 10 * 1000;
    private final int SEND_BUFFER_SIZE_LIMIT = 512 * 1024;


    @Override
    public void addSession(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("userId");
        activeSessions.compute(userId, (key, sessions) -> {
            if (sessions == null) {
                sessions = ConcurrentHashMap.newKeySet();
            }
            sessions.add(session);
            return sessions;
        });
    }

    @Override
    public void removeSession(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            activeSessions.computeIfPresent(userId, (key, sessions) -> {
                sessions.remove(session);
                return sessions.isEmpty() ? null : sessions;
            });
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, SendRequestMessageDTO sendRequestMessageDTO) {
        Long userId = (Long) session.getAttributes().get("userId");


        MessageDTO messageToStorage =
                sendRequestMessageDTOMapper.toMessageToStorageDTO(sendRequestMessageDTO, userId);


        sendMessageService.sendMessage(messageToStorage);


    }

    @Override
    public void receiveMessagesToMembers(MessageDTO messageDTO) {
        Long senderId = messageDTO.getMessage().getSenderId();
        Long chatId = messageDTO.getMessage().getChatId();
        if (!chatServiceClient.isUserInChat(senderId, chatId)) {
            return;
        }
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(chatId);

        ReceiveMessageDTO receiveMessage = messageDTOMapper.toReceiveMessageDTO(messageDTO);

        for (Long recipientId : recipientUsers) {
            Set<WebSocketSession> recipientSessions = activeSessions.get(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (WebSocketSession recipientSession : recipientSessions) {
                    receiveMessage(recipientSession, receiveMessage);
                }
            }
        }

    }

    private void receiveMessage(WebSocketSession session, ReceiveMessageDTO receiveMessageDTO)  {

        try {

            String jsonResponse = objectMapper.writeValueAsString(receiveMessageDTO);
            if (session.isOpen()) {

                //вот би сделать неблокирующее отправление по вебсокетам
                new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT)
                        .sendMessage(new TextMessage(jsonResponse));
            }

        } catch (IOException e) {
            System.err.println("Ошибка отправки сообщения по ws, закрытие соединения: " + e.getMessage());

            try {
                if (!session.isOpen()) {
                    session.close();
                }
            } catch (IOException ignored){}
        }
    }


}
