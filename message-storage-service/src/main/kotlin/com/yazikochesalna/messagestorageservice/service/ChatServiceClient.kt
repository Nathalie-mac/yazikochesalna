package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.common.service.JwtService
import com.yazikochesalna.messagestorageservice.config.properties.ChatServiceProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.time.Duration
private const val MAPPING_STRING: String = "%s/api/v1/chats/check/%d/%d"

@EnableConfigurationProperties(ChatServiceProperties::class)
@Component
open class ChatServiceClient(private val webClient: WebClient,
    private val jwtService: JwtService,
    private val chatServiceProperties: ChatServiceProperties,
    ) {
    private val log: Logger = LoggerFactory.getLogger(ChatServiceClient::class.java)


    fun checkUserInChat(userId: Long, chatId:Long): Boolean {
        var url = String.format(MAPPING_STRING, chatServiceProperties.url, chatId, userId);
        return kotlin.runCatching {
            webClient.get()
                .uri(url)
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
                            log.error("Unexpected HTTP code from ChatService: ${response.statusCode()}")
                            response.createError()
                        }
                    }
                }
                .onErrorResume { e ->
                    log.error("Fatal error in ChatService: ${e.message}")
                    Mono.just(false)
                }
                .block(Duration.ofSeconds(10)) ?: false
        }.getOrElse { e ->
            log.error("Could not proccess answer from ChatService ${e.message}")
            false
        }
    }
}