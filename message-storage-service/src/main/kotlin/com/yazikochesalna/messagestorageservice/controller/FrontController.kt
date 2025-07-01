package com.yazikochesalna.messagestorageservice.controller

import com.yazikochesalna.messagestorageservice.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/messages")
class FrontController(
    private val messageService: MessageService
) {
    private val maxLimit: Int = 100

    @GetMapping("/{chatId}")
    fun getMessages(
        @PathVariable chatId: Long,
        @RequestParam(required = false) limitUp: Int?,
        @RequestParam(required = false) limitDown: Int?,
        @RequestParam(required = false) cursor: Long?
    ): ResponseEntity<Any> {
        // Проверяем существование чата
//        if (!chatService.chatExists(chatId)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Чат не найден")
//        }

        //Проверяем есть ли пользователь


        // Валидация параметров limit
        if ((limitUp != null && (limitUp < 0 || limitUp > maxLimit)) ||
            (limitDown != null && (limitDown < 0 || limitDown > maxLimit))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Limit не должен превышать значение $maxLimit!")
        }
        if ((cursor == null)|| ((cursor != null) && cursor <0)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cursor должен быть >0!")
        }

        // Получаем сообщения
        val messages = messageService.getMessagesAroundCursor(
            chatId = chatId,
            cursor = cursor,
            limitUp = limitUp ?: 0,
            limitDown = limitDown ?: 0
        )

        return ResponseEntity.ok(mapOf("messages" to messages))
    }
}