package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor

@NoArgsConstructor
class PayLoadNoticeDTO(
    var memberId: Long,
    var chatId: Long
) : PayLoadDTO