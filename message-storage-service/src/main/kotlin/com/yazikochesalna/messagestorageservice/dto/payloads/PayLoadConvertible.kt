package com.yazikochesalna.messagestorageservice.dto.payloads

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.BaseMessage
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.LocalDateTime
import java.util.UUID

interface PayLoadConvertible {
    fun toMessage(messageId: UUID, type: MessageType, timestamp: LocalDateTime): Pair<Message, List<Attachment>>

    fun toMessageJsonFormatDTO(message: BaseMessage, attachments: List<Attachment>): MessagesJsonFormatDTO
}