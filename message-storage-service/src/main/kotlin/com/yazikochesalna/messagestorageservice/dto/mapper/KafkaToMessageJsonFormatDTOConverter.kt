package com.yazikochesalna.messagestorageservice.dto.mapper

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.LocalDateTime
import java.util.*

class KafkaToMessageJsonFormatDTOConverter {
    fun convertToMessageJsonFormatDTO(record: String): MessagesJsonFormatDTO{
        return MessagesJsonFormatDTO(UUID.randomUUID(), MessageType.MESSAGE, LocalDateTime.now(), PayLoadMessageDTO(1, 1, "gegege"))
    }
}