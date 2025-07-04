package com.yazikochesalna.messagestorageservice.dto

import java.time.LocalDateTime
import java.util.*

class MessagesToFrontDTO(
    val messageId: UUID,
    val senderId: Long,
    val chatId: Long,
    val timestamp: LocalDateTime,
) {

}