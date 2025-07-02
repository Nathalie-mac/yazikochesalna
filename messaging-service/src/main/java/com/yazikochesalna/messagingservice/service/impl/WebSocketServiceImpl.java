package com.yazikochesalna.messagingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazikochesalna.messagingservice.dto.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.SendResponseMessageDTO;
import com.yazikochesalna.messagingservice.dto.SendResponseResultType;
import com.yazikochesalna.messagingservice.dto.convertors.MessageToStorageDTOToReceiveMessageDTOConvertor;
import com.yazikochesalna.messagingservice.dto.convertors.SendRequestMessageDTOToMessageToStorageDTOConvertor;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.exception.ReceiveMessageException;
import com.yazikochesalna.messagingservice.exception.ReceiveSendResponseException;
import com.yazikochesalna.messagingservice.exception.UserNotHaveAccessToChatException;
import com.yazikochesalna.messagingservice.service.ChatServiceClient;
import com.yazikochesalna.messagingservice.service.SendMessageToStorageService;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

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
        if (!chatServiceClient.isUserInChat(userId, sendRequestMessageDTO.getChatId())) {
            throw new UserNotHaveAccessToChatException();
        }
        //TODO:подумоть
        MessageToStorageDTO messageToStorage =
                SendRequestMessageDTOToMessageToStorageDTOConvertor.convert(sendRequestMessageDTO, userId);


        sendMessageToStorageService.sendMessageToStorage(messageToStorage);
        List<Long> recipientUsers = chatServiceClient.getUsersByChatId(sendRequestMessageDTO.getChatId());

        receiveSendResponse(session, SendResponseResultType.OK);

        for (Long recipientId : recipientUsers) {
            Set<WebSocketSession> recipientSessions = activeSessions.get(recipientId);
            if (recipientSessions != null && !recipientSessions.isEmpty()) {
                for (WebSocketSession recipientSession : recipientSessions) {
                    if (recipientSession.isOpen()) {
                        //TODO:подумоть
                        ReceiveMessageDTO receiveMessage =
                                MessageToStorageDTOToReceiveMessageDTOConvertor.convert(messageToStorage);
                        receiveMessage(recipientSession, receiveMessage);
                    }
                }
            } else {
                // логика с другими инстансами
            }
        }
    }

    @Override
    public void receiveSendResponse(WebSocketSession session, SendResponseResultType sendResponseResultType) {
        try {
            long requestId = requestIdCounter.incrementAndGet();

            SendResponseMessageDTO response = new SendResponseMessageDTO(
                    requestId,
                    sendResponseResultType
            );

            String jsonResponse = objectMapper.writeValueAsString(response);

            if (session.isOpen()) {
                session.sendMessage(new TextMessage(jsonResponse));
            }
        } catch (IOException e) {
            throw new ReceiveSendResponseException();
        }
    }


    @Override
    public void receiveMessage(WebSocketSession session, ReceiveMessageDTO receiveMessageDTO) {

        try {

            String jsonResponse = objectMapper.writeValueAsString(receiveMessageDTO);
            session.sendMessage(new TextMessage(jsonResponse));

        } catch (IOException e) {
            throw new ReceiveMessageException() ;
        }
    }
}
