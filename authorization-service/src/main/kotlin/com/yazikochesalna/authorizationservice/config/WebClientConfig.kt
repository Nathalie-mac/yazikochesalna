package com.yazikochesalna.authorizationservice.config

import com.yazikochesalna.authorizationservice.com.yazikochesalna.authorizationservice.config.properties.UserServiceProperties
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@RequiredArgsConstructor
class WebClientConfig(
    private val userServiceProperties: UserServiceProperties
) {
    @Bean
    fun userServiceWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(userServiceProperties.url)
            .clientConnector(ReactorClientHttpConnector(reactor.netty.http.client.HttpClient.create())).build()
    }
}