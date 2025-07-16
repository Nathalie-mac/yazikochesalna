package com.yazikochesalna.messagestorageservice.dto.payloads

import java.util.UUID

data class PayLoadNewChatAvatarDTO(
    var avatarId: UUID,
    var memberId: Long,
    var chatId: Long
): PayLoadDTO
