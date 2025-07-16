package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor

@NoArgsConstructor
data class PayLoadNoticeDTO(
    var memberId: Long,
    override var chatId: Long
) : PayLoadDTO