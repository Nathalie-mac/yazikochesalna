package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.common.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration



@Component
open class ChatServiceClient(private val webClient: WebClient,
    private val jwtService: JwtService) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ChatServiceClient::class.java)
    }

    fun checkUserInChat(userId: Long, chatId:Long): Boolean {
        return try {
            webClient.get()
                .uri("http://localhost:8080/api/v1/chats/check/$chatId/$userId")
                .headers { headers ->
                    headers.setBearerAuth(jwtService.generateServiceToken())
                }
                .exchangeToMono { response ->
                    when {
                        response.statusCode().is2xxSuccessful -> Mono.just(true)
                        response.statusCode() == HttpStatus.NOT_FOUND -> {
                            //log.debug("Все гуд chatId = $chatId")
                            Mono.just(false)
                        }
                        response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR -> {
                            //log.error("Все бэд 500 chatId=$chatId, userId=$userId")
                            Mono.just(false)
                        }
                        else -> {
                            log.error("Неожиданный код от ChatService: ${response.statusCode()}")
                            response.createError()
                        }
                    }
                }
                .onErrorResume { e ->
                    log.error("Критическая ошибка в ChatService: ${e.message}")
                    Mono.just(false)
                }
                .block(Duration.ofSeconds(10)) ?: false
        } catch (e: Exception) {
            //log.error("Критическая ошибка в block()", e)
            log.error("Не смогли обработать от ChatService ${e.message}")
            false
        }
    }
}