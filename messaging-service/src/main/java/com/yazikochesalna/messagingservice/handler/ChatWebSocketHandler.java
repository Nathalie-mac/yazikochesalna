package com.yazikochesalna.messagingservice.handler;

import com.yazikochesalna.messagingservice.processor.TextMessageProcessor;
import com.yazikochesalna.messagingservice.service.WebSocketSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final TextMessageProcessor textMessageProcessor;
    private final WebSocketSessionService webSocketSessionService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessionService.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        textMessageProcessor.processMessage(session, message);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessionService.removeSession(session);
    }
}
