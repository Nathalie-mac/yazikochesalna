package com.yazikochesalna.messagestorageservice.dto

import lombok.Builder
import lombok.Data

@Builder
@Data
data class NewestMessagesListDTO(
    var messages: List<NewestMessageDTO>
)
