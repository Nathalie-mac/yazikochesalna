package com.yazikochesalna.messagestorageservice.dto.payloads

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.BaseMessage
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.AttachmentType
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.*

@NoArgsConstructor
data class PayLoadPinDTO(
    var pinMessageId: UUID,
    var memberId: Long,
    override var chatId: Long
): PayLoadDTO, PayLoadConvertible{
    override fun toMessage(
        messageId: UUID,
        type: MessageType,
        timestamp: LocalDateTime
    ): Pair<Message, List<Attachment>> {
        val message = Message(
            id = messageId,
            type = type,
            sendTime = timestamp,
            senderId = memberId,
            chatId = chatId,
            text = null
        )
        val attachment = Attachment(
            id = pinMessageId,
            messageId = message.id,
            type = AttachmentType.PIN
        )
        return message to listOf(attachment)
    }

    override fun toMessageJsonFormatDTO(message: BaseMessage, attachments: List<Attachment>): MessagesJsonFormatDTO =
        MessagesJsonFormatDTO(
            messageId = message.id,
            type = message.type,
            timestamp = message.sendTime,
            payload = this
        )
}
