package com.yazikochesalna.authorizationservice.service

import com.yazikochesalna.authorizationservice.config.properties.UserServiceProperties
import com.yazikochesalna.authorizationservice.dto.userservice.CreateUserRequest
import com.yazikochesalna.authorizationservice.dto.userservice.CreateUserResponse
import com.yazikochesalna.authorizationservice.exception.userservice.UserConflictCustomException
import com.yazikochesalna.authorizationservice.exception.userservice.UserCreationCustomException
import com.yazikochesalna.common.service.JwtService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class UserServiceClientImpl
    (
    private val chatServiceWebClient: WebClient,
    private val jwtService: JwtService,
    private val userServiceProperties: UserServiceProperties
) : UserServiceClient {

    val SAVE_USER_URL_FORMAT: String = "%s/api/v1/users"

    override fun saveUser(username: String): Long {
        val url = String.format(SAVE_USER_URL_FORMAT, userServiceProperties.url)
        val request = CreateUserRequest(username)

        try {
            val createUserResponse = chatServiceWebClient.post()
                .uri(url)
                .headers { headers: HttpHeaders ->
                    headers.setBearerAuth(
                        jwtService.generateServiceToken()
                    )
                }
                .bodyValue(request)
                .retrieve()
                .onStatus({ code -> code == HttpStatus.CONFLICT }, { resp ->
                    Mono.error(UserConflictCustomException("User with username ${username} already exists"))
                })
                .bodyToMono(CreateUserResponse::class.java)
                .block()

            // обработка успешного ответа (статус 201)
            return createUserResponse!!.id

        } catch (e: UserConflictCustomException) {
            // Обработка конфликта (409)
            throw e
        } catch (e: Exception) {
            throw UserCreationCustomException(e);
        }
    }
}