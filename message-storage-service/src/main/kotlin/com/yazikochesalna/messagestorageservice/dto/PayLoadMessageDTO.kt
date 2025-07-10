package com.yazikochesalna.messagestorageservice.dto

data class PayLoadMessageDTO(
    val senderId: Long,
    val chatId: Long,
    val text: String
): PayLoadDTO
