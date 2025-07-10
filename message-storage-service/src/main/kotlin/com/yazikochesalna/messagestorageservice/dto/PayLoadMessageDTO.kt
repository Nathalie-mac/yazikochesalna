package com.yazikochesalna.messagestorageservice.dto

import lombok.NoArgsConstructor

@NoArgsConstructor
class PayLoadMessageDTO(
    var senderId: Long,
    var chatId: Long,
    var text: String
): PayLoadDTO
