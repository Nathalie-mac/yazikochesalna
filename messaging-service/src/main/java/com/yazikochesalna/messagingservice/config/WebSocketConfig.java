package com.yazikochesalna.messagingservice.config;

import com.yazikochesalna.messagingservice.config.properties.ChatServiceProperties;
import com.yazikochesalna.messagingservice.config.properties.FrontendProperties;
import com.yazikochesalna.messagingservice.handler.ChatWebSocketHandler;
import com.yazikochesalna.messagingservice.interseptor.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;
    private final ChatServiceProperties chatServiceProperties;
    private final FrontendProperties frontendProperties;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws")
                .addInterceptors(webSocketHandshakeInterceptor)
                .setAllowedOriginPatterns(chatServiceProperties.getUrl(), frontendProperties.getUrl());
    }


}
