package com.yazikochesalna.messagingservice.interseptor;

import com.yazikochesalna.messagingservice.exception.InvalidWebSocketTokenCustomException;
import com.yazikochesalna.messagingservice.service.RedissonWebSocketTokenService;
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
    public static final String TOKEN_QUERY_PREFIX = "token=";
    public static final String USER_ID_SESSION_ATTRIBUTE_NAME = "userId";
    public static final String ERROR_MESSAGE_CUSTOM_HEADER_NAME = "X-Error-Message";
    private final RedissonWebSocketTokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        try {
            String query = request.getURI().getQuery();
            if (query == null || !query.contains(TOKEN_QUERY_PREFIX)) {
                throw new InvalidWebSocketTokenCustomException();
            }
            String token = query.replace(TOKEN_QUERY_PREFIX, "");
            Long userId = tokenService.validateAndGetUserId(token);
            attributes.put(USER_ID_SESSION_ATTRIBUTE_NAME, userId);
            return true;
        } catch (InvalidWebSocketTokenCustomException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            response.getHeaders().add(ERROR_MESSAGE_CUSTOM_HEADER_NAME, e.getMessage());
            return false;
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}