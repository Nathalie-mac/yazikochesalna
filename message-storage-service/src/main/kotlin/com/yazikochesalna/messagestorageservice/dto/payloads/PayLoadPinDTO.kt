package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
data class PayLoadPinDTO(
    var pinMessageId: UUID,
    var memberId: Long,
    override var chatId: Long
): PayLoadDTO
