package com.yazikochesalna.messagestorageservice.converter

import com.yazikochesalna.messagestorageservice.dto.MessagesToFrontDTO
import com.yazikochesalna.messagestorageservice.model.db.Message

class MessagesConverter {
    fun convertMessageToMessageFrontDTO(Message : Message) : MessagesToFrontDTO {
        return MessagesToFrontDTO()
    }
}