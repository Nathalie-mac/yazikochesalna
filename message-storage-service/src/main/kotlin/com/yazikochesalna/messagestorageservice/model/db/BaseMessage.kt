package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.LocalDateTime
import java.util.UUID

interface BaseMessage{
    val id: UUID
    val type: MessageType
    val sendTime: LocalDateTime
    val senderId: Long
    val chatId: Long
    val text: String?
    val markedToDelete: Boolean
}