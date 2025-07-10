package com.yazikochesalna.messagestorageservice.config

import com.yazikochesalna.messagestorageservice.config.properties.ChatServiceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
open class WebClientConfig(
) {
    @Bean
    open fun webClient(): WebClient {
        return WebClient.builder()
            //.defaultHeaders { headers -> headers.setBearerAuth("") }
            .build()
    }
}