package com.yazikochesalna.messagestorageservice.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import com.yazikochesalna.messagestorageservice.dto.serializers.MessageJsonFormatDeserializer
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.LocalDateTime
import java.util.*

//@JsonDeserialize(using = MessageJsonFormatDeserializer::class)
data class MessagesJsonFormatDTO(
    val messageId: UUID?,
    val type: MessageType?,
    val timestamp: LocalDateTime?,
    val payload: PayLoadDTO?
) {
//    fun <T : PayLoadDTO?> getPayload(): T {
//        return payload as T
//    }
}