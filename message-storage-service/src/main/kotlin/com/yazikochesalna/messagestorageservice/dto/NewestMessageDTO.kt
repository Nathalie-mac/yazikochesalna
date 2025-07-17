package com.yazikochesalna.messagestorageservice.dto

import lombok.Builder
import lombok.Data

@Builder
@Data
data class NewestMessageDTO(
    var chatId: Long,
    var lastMessage: MessagesJsonFormatDTO?
)
