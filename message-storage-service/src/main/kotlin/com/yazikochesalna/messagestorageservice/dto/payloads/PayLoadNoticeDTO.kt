package com.yazikochesalna.messagestorageservice.dto.payloads

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.BaseMessage
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.*

@NoArgsConstructor
data class PayLoadNoticeDTO(
    var memberId: Long,
    override var chatId: Long
) : PayLoadDTO, PayLoadConvertible{
    override fun toMessage(
        messageId: UUID,
        type: MessageType,
        timestamp: LocalDateTime
    ): Pair<Message, List<Attachment>> =
        Message(
            id = messageId,
            type = type,
            sendTime = timestamp,
            senderId = memberId,
            chatId = chatId,
            text = null
        ) to emptyList()

    override fun toMessageJsonFormatDTO(message: BaseMessage, attachments: List<Attachment>): MessagesJsonFormatDTO =
        MessagesJsonFormatDTO(
            messageId = message.id,
            type = message.type,
            timestamp = message.sendTime,
            payload = this
        )
}