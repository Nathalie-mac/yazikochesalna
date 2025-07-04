package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.common.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
open class ChatServiceClient(private val webClient: WebClient,
    private val jwtService: JwtService) {
    fun checkUserInChat(userId: Long, chatId:Long): Mono<Boolean> {
        return webClient.get()
            .uri("http://localhost:8080/api/v1/chats/check/$chatId/$userId")
            .headers{headers -> headers.setBearerAuth(jwtService.generateServiceToken())}
            .retrieve()
            .onStatus({httpStatus -> httpStatus.is4xxClientError}){Mono.error(RuntimeException("No user in chat"))}
            .onStatus({httpStatus -> httpStatus.is5xxServerError}){Mono.error(RuntimeException("Something wrong at ChatService"))}
            .toBodilessEntity()
            .map { it.statusCode.is2xxSuccessful}
    }
}