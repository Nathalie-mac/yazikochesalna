package com.yazikochesalna.messagingservice.interseptor;

import com.yazikochesalna.messagingservice.exception.InvalidWebSocketTokenException;
import com.yazikochesalna.messagingservice.service.WebSocketTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final WebSocketTokenService tokenService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String query = request.getURI().getQuery();
            if (query == null || !query.contains("token=")) {
                throw new InvalidWebSocketTokenException();
            }
            String token = query.replace("token=", "");
            Long userId = tokenService.validateAndGetUserId(token);
            attributes.put("userId", userId);
            return true;
        } catch (InvalidWebSocketTokenException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            response.getHeaders().add("X-Error-Message", e.getMessage());
            return false;
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }


}