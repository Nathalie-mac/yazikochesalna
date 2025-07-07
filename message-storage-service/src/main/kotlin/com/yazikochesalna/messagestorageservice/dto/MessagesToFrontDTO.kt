package com.yazikochesalna.messagestorageservice.dto

import java.time.LocalDateTime
import java.util.*

class MessagesToFrontDTO(
    val messageId: UUID,
    val senderId: Long,
    val text: String,
    val timestamp: LocalDateTime,
) {

}