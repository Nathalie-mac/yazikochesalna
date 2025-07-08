package com.yazikochesalna.messagestorageservice.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

//@JsonSerialize(using = MessagesJSONSerializer::class)
data class MessagesJsonFormatDTO(
    val messageId: UUID?,
    val type: MessageType?,
    val timestamp: LocalDateTime?,
    val payload: PayLoadDTO?
) {
//    fun <T : PayLoadDTO?> getPayLoad(): T {
//        return payload as T
//    }
}