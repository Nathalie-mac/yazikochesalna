package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {

    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessageToKafka(WebSocketSession session, MessageDTO messageDTO);


    void broadcastMessageToParticipants(MessageDTO messageDTO);

    void sendNotificationToKafka(MessageDTO notificationDTO);


}
