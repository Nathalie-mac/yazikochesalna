package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor

@NoArgsConstructor
data class PayLoadMessageDTO(
    var senderId: Long,
    override var chatId: Long,
    var text: String,
    var attachments: List<MessageAttachmentDTO>?
): PayLoadDTO
