package com.yazikochesalna.messagestorageservice.dto.payloads

import java.util.*

data class PayLoadPinDTO(
    var pinMessageId: UUID,
    var memberId: Long,
    var chatId: Long
): PayLoadDTO
