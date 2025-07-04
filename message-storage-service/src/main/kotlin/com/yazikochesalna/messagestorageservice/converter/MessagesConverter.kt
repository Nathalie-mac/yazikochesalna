package com.yazikochesalna.messagestorageservice.converter

import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import com.yazikochesalna.messagestorageservice.model.db.Message
import java.time.LocalDateTime
import java.util.*

class MessagesConverter {
    fun convertMessageToMessageFrontDTO(Message : Message) : MessagesToFrontDTO {
        return MessagesToFrontDTO(UUID.randomUUID(), 0, 0, LocalDateTime.now())
    }
}