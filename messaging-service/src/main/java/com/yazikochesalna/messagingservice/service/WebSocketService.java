package com.yazikochesalna.messagingservice.service;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {

    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessage();

    void receiveMessage();
}
