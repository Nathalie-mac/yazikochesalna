package com.yazikochesalna.messagestorageservice.dto.payloads

import lombok.NoArgsConstructor
import java.util.UUID

@NoArgsConstructor
data class PayLoadNewChatAvatarDTO(
    var avatarId: UUID,
    var memberId: Long,
    override var chatId: Long
): PayLoadDTO
