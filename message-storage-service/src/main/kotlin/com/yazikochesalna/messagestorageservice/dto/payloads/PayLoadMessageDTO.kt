package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor

@NoArgsConstructor
class PayLoadMessageDTO(
    var senderId: Long,
    var chatId: Long,
    var text: String,
    var attachments: List<MessageAttachmentDTO>?
): PayLoadDTO
