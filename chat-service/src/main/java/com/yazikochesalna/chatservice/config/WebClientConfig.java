package com.yazikochesalna.chatservice.config;

import com.yazikochesalna.chatservice.config.properties.UserServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
class WebClientConfig {
    private final UserServiceProperties userServiceProperties;
    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
            .baseUrl(userServiceProperties.getUrl())
            .clientConnector(new ReactorClientHttpConnector(HttpClient.create())).build();
    }
}