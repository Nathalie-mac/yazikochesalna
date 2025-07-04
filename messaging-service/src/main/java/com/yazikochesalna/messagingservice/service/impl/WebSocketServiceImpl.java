package com.yazikochesalna.messagingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.callback.MessageToStorageCallback;
import com.yazikochesalna.messagingservice.dto.mapper.SendRequestMessageDTOMapper;
import com.yazikochesalna.messagingservice.dto.mapper.MessageToStorageDTOMapper;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.response.SendResponseMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.response.SendResponseResultType;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.exception.ReceiveMessageCustomException;
import com.yazikochesalna.messagingservice.exception.ReceiveSendResponseCustomException;
import com.yazikochesalna.messagingservice.exception.UserNotHaveAccessToChatCustomException;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import com.yazikochesalna.messagingservice.service.SendMessageToStorageService;
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
    private final SendMessageToStorageService sendMessageToStorageService;
    private final SendRequestMessageDTOMapper sendRequestMessageDTOMapper;
    private final MessageToStorageDTOMapper messageToStorageDTOMapper;
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
//        if (!chatServiceClient.isUserInChat(userId, sendRequestMessageDTO.getChatId())) {
//            throw new UserNotHaveAccessToChatCustomException();
//        }

        MessageToStorageDTO messageToStorage =
                sendRequestMessageDTOMapper.toMessageToStorageDTO(sendRequestMessageDTO, userId);
        ReceiveMessageDTO receiveMessage =
                messageToStorageDTOMapper.toReceiveMessageDTO(messageToStorage);

        sendMessageToStorageService.sendMessageToStorage(messageToStorage, new MessageToStorageCallback() {
            @Override
            public void onSuccess() {
                try {
                    receiveSendResponse(session, SendResponseResultType.OK);
                    sendMessageToChat(sendRequestMessageDTO.getChatId(), receiveMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                receiveSendResponse(session, SendResponseResultType.NOT_SENT_TO_STORAGE);
            }
        });


    }

    @Override
    public void sendMessageToChat(Long chatId, ReceiveMessageDTO receiveMessage) {
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(chatId);
        for (Long recipientId : recipientUsers) {
            // тут будет лочится T-T (можно переделать мапу(chatId, session)
            // и переделать логику подключения, но там надо обрабатывать приколы с новыми чатам
            Set<WebSocketSession> recipientSessions = activeSessions.get(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (WebSocketSession recipientSession : recipientSessions) {
                    receiveMessage(recipientSession, receiveMessage);

                }
            }
            // логика с другими инстансами
        }
    }

    @Override
    public void receiveSendResponse(WebSocketSession session, SendResponseResultType sendResponseResultType) {
        try {
            long requestId = requestIdCounter.incrementAndGet();

            SendResponseMessageDTO response = new SendResponseMessageDTO();
            response.setRequestId(requestId);
            response.setResult(sendResponseResultType);

            String jsonResponse = objectMapper.writeValueAsString(response);

            if (session.isOpen()) {
                new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT)
                        .sendMessage(new TextMessage(jsonResponse));
            }
        } catch (IOException e) {
            throw new ReceiveSendResponseCustomException();
        }
    }


    @Override
    public void receiveMessage(WebSocketSession session, ReceiveMessageDTO receiveMessageDTO) {

        try {

            String jsonResponse = objectMapper.writeValueAsString(receiveMessageDTO);
            if (session.isOpen()) {
                //вот би сделать неблокирующее отправление по вебсокетам
                new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT)
                        .sendMessage(new TextMessage(jsonResponse));
            }

        } catch (IOException e) {
            throw new ReceiveMessageCustomException();
        }
    }


}
