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
data class PayLoadMessageDTO(
    var senderId: Long,
    override var chatId: Long,
    var text: String,
    var attachments: List<MessageAttachmentDTO>?
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
            senderId = senderId,
            chatId = chatId,
            text = text
        )
        val attachments = attachments?.map {
            Attachment(
                id = it.id,
                messageId = messageId,
                type = it.type
            )
        } ?: emptyList()
        return message to attachments
    }

    override fun toMessageJsonFormatDTO(message: BaseMessage, attachments: List<Attachment>): MessagesJsonFormatDTO =
        MessagesJsonFormatDTO(
            messageId = message.id,
            type = message.type,
            timestamp = message.sendTime,
            payload = this
        )
}
