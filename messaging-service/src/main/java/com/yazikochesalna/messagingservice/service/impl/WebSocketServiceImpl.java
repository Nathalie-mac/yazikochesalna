package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final Map<Long, Set<WebSocketSession>> activeSessions = new ConcurrentHashMap<>();

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
    public void sendMessage() {

    }

    @Override
    public void receiveMessage() {

    }
}
