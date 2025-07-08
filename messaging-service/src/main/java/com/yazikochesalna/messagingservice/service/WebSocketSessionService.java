package com.yazikochesalna.messagingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSocketSessionService {
    public static final String USER_ID_SESSION_ATTRIBUTE_NAME = "userId";

    private final Map<Long, Set<ConcurrentWebSocketSessionDecorator>> activeSessions = new ConcurrentHashMap<>();

    private final int SEND_TIME_LIMIT = 10 * 1000;
    private final int SEND_BUFFER_SIZE_LIMIT = 512 * 1024;

    public void addSession(WebSocketSession session) {
        var userId = getUserId(session);
        var concurrentSession = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, SEND_BUFFER_SIZE_LIMIT);
        Set<ConcurrentWebSocketSessionDecorator> sessions = activeSessions.get(userId);
        if (sessions == null) {
            sessions = new CopyOnWriteArraySet<>();
            sessions.add(concurrentSession);
            activeSessions.put(userId, sessions);
        } else {
            sessions.add(concurrentSession);
        }
    }

    public void removeSession(WebSocketSession session) {
        var userId = getUserId(session);
        Set<ConcurrentWebSocketSessionDecorator> sessions = activeSessions.get(userId);
        if (sessions != null) {
            Set<WebSocketSession> updatedSessions = sessions.stream()
                    .filter(decoratedSession ->
                            !decoratedSession.getDelegate().equals(session))
                    .collect(Collectors.toSet());
            if (updatedSessions.isEmpty()) {
                activeSessions.remove(userId);
            }
        }
    }

    public ConcurrentWebSocketSessionDecorator getConcurrentSession(WebSocketSession session) {
        var userId = (Long) session.getAttributes().get("userId");
        Set<ConcurrentWebSocketSessionDecorator> userSessions = activeSessions.get(userId);
        if (userSessions != null) {
            return userSessions.stream()
                    .filter(decoratedSession ->
                            decoratedSession.getDelegate().equals(session))
                    .findAny().get();
        }
        return null;

    }

    public Set<ConcurrentWebSocketSessionDecorator> getUserSessions(Long userId) {
        return activeSessions.get(userId);

    }

    public Long getUserId(WebSocketSession session) {
        return (Long) session.getAttributes().get(USER_ID_SESSION_ATTRIBUTE_NAME);
    }


}
