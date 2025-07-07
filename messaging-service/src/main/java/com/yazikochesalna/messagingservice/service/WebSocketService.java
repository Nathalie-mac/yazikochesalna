package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.response.ResponseResultType;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {

    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessage(WebSocketSession session, MessageDTO messageDTO);
    void sendErrorResponse(WebSocketSession session, ResponseResultType responseResultType, Long requestId);

    void sendMessage(MessageDTO notificationDTO);

    void broadcastMessageToParticipants(MessageDTO messageDTO);


}
