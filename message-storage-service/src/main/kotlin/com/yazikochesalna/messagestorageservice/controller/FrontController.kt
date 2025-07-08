package com.yazikochesalna.messagestorageservice.controller

import com.yazikochesalna.common.authentication.JwtAuthenticationToken
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.service.MessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

const val MAX_LIMIT: Int = 100

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
@SecurityRequirement(name = "bearerAuth")
class FrontController(
    private val messageService: MessageService
) {
    //private val maxLimit: Int = 100

    @GetMapping("/{chatId}")
    @Operation(
        summary = "Получить сообщения чата: N выше и M ниже относительно курсора",
        description = "Возвращает список имеющихся сообщений из заданного диапазона, которые уже хранятся в БД"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Информация о сообщениях получена",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MessagesJsonFormatDTO::class)
                    )]),
            ApiResponse(
                responseCode = "400",
                description = "Некорректные параметры: лимиты превышают 100 или null, или cursor = null!"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Чат не найден"
            )
        ]
    )
    fun getMessages(
        @PathVariable chatId: Long,
        @RequestParam(required = false) limitUp: Int?,
        @RequestParam(required = false) limitDown: Int?,
        @RequestParam(required = false) cursor: UUID?
    ): ResponseEntity<List<MessagesJsonFormatDTO>?> {
        //Тащим id пользователя
        var userId = (SecurityContextHolder.getContext().authentication as? JwtAuthenticationToken)?.userId

        // Валидация параметров limit & cursor
        if (validateLimit(limitUp, limitDown)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        if (cursor == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }


        //Получаем сообщения
        return runCatching {
            val body = userId?.let {
                messageService.getMessagesAroundCursor(
                    userId = it,
                    chatId = chatId,
                    cursor = cursor,
                    limitUp = limitUp ?: 0,
                    limitDown = limitDown ?: 0
                )
            }
            ResponseEntity.ok(body)
        }.getOrElse { e ->
            when (e) {
                is ResponseStatusException -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }


    }

    private fun validateLimit(limitUp: Int?, limitDown: Int?): Boolean{
        return ((limitUp != null && (limitUp < 0 || limitUp > MAX_LIMIT)) ||
                (limitDown != null && (limitDown < 0 || limitDown > MAX_LIMIT)))
    }
}