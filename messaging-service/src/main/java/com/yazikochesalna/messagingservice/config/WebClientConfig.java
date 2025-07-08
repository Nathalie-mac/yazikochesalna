package com.yazikochesalna.messagingservice.config;

import com.yazikochesalna.messagingservice.config.properties.ChatServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ChatServiceProperties chatServiceProperties;

    @Bean
    public WebClient chatServiceWebClient() {
        return WebClient.builder()
                .baseUrl(chatServiceProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create())).build();
    }
}
