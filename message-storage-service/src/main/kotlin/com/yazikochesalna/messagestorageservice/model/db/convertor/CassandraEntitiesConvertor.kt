package com.yazikochesalna.messagestorageservice.model.db.convertor

import com.datastax.oss.driver.api.core.cql.Row
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.payloads.*
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInPayLoadException
import com.yazikochesalna.messagestorageservice.exception.customexceptions.NullCassandraFiledException
import com.yazikochesalna.messagestorageservice.model.db.Attachment
import com.yazikochesalna.messagestorageservice.model.db.BaseMessage
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.db.MessageByChat
import com.yazikochesalna.messagestorageservice.model.enums.AttachmentType
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.time.LocalDateTime

@Component
class CassandraEntitiesConvertor {

    fun convertToMessagesJsonFormatDto(message: BaseMessage, attachments: List<Attachment>): MessagesJsonFormatDTO {
        val payload = when (message.type) {
            MessageType.MESSAGE -> PayLoadMessageDTO(
                senderId = message.senderId,
                chatId = message.chatId,
                text = message.text ?: "",
                attachments = attachments.map {
                    MessageAttachmentDTO(type = it.type, id = it.id)
                }
            )
            MessageType.NEW_MEMBER, MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
                memberId = message.senderId,
                chatId = message.chatId
            )
            MessageType.PIN -> PayLoadPinDTO(
                pinMessageId = attachments.first().id,
                chatId = message.chatId,
                memberId = message.senderId
            )
            MessageType.NEW_CHAT_AVATAR -> PayLoadNewChatAvatarDTO(
                avatarId = attachments.first().id,
                chatId = message.chatId,
                memberId = message.senderId
            )
        }
        return payload.toMessageJsonFormatDTO(message, attachments)
    }
//        MessagesJsonFormatDTO(
//            messageId = message.id,
//            type = message.type,
//            timestamp = message.sendTime,
//            payload = when (message.type){
//                MessageType.MESSAGE -> PayLoadMessageDTO(
//                    senderId = message.senderId,
//                    chatId = message.chatId,
//                    text = message.text?: "",
//                    attachments = attachments.map { MessageAttachmentDTO(
//                        type = it.type,
//                        id = it.id,
//                    ) }
//                )
//                MessageType.NEW_MEMBER,
//                MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
//                    memberId = message.senderId,
//                    chatId = message.chatId,
//                )
//                MessageType.PIN -> PayLoadPinDTO(
//                    pinMessageId = attachments.first().id,
//                    chatId = message.chatId,
//                    memberId = message.senderId,
//
//                )
//                MessageType.NEW_CHAT_AVATAR -> PayLoadNewChatAvatarDTO(
//                    avatarId = attachments.first().id,
//                    chatId = message.chatId,
//                    memberId = message.senderId,
//                )
//            }
//        )

//    //пиупиупиу
//    fun convertToMessagesJsonFormatDto(messageByChat: MessageByChat): MessagesJsonFormatDTO {
//        return MessagesJsonFormatDTO(
//            messageId = messageByChat.id,
//            type = messageByChat.type,
//            timestamp = messageByChat.sendTime,
//            payload = when (messageByChat.type){
//                MessageType.MESSAGE -> PayLoadMessageDTO(
//                    senderId = messageByChat.senderId,
//                    chatId = messageByChat.chatId,
//                    text = messageByChat.text?: "",
//                    attachments = emptyList()
//                )
//                MessageType.NEW_MEMBER,
//                MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
//                    memberId = messageByChat.senderId,
//                    chatId = messageByChat.chatId,
//                )
//                else -> throw IllegalArgumentException("Not supported message type ${messageByChat.type}")
//            }
//        )
//    }

    fun convertToMessageWithAttachments(messagesJsonFormatDTO: MessagesJsonFormatDTO): Pair<Message, List<Attachment>> {
        val payload = messagesJsonFormatDTO.payload as? PayLoadConvertible
            ?: throw ErrorInPayLoadException("Unsupported payload type ${messagesJsonFormatDTO.payload.javaClass.name}")
        return payload.toMessage(
            messageId = messagesJsonFormatDTO.messageId,
            type = messagesJsonFormatDTO.type,
            timestamp = messagesJsonFormatDTO.timestamp,
        )
    //        when (messagesJsonFormatDTO.type) {
//            MessageType.MESSAGE -> {
//                val payload = messagesJsonFormatDTO.payload as PayLoadMessageDTO
//                val message = Message(
//                    id = messagesJsonFormatDTO.messageId,
//                    type = messagesJsonFormatDTO.type,
//                    sendTime = messagesJsonFormatDTO.timestamp,
//                    senderId = payload.senderId,
//                    chatId = payload.chatId,
//                    text = payload.text
//                )
//
//                val attachments = payload.attachments?.map { attachmentDto ->
//                    Attachment(
//                        id = attachmentDto.id,
//                        messageId = messagesJsonFormatDTO.messageId,
//                        type = attachmentDto.type
//                    )
//                } ?: emptyList()
//
//                message to attachments
//            }
//
//            MessageType.NEW_MEMBER, MessageType.DROP_MEMBER -> {
//                val payload = messagesJsonFormatDTO.payload as PayLoadNoticeDTO
//                Message(
//                    id = messagesJsonFormatDTO.messageId,
//                    type = messagesJsonFormatDTO.type,
//                    sendTime = messagesJsonFormatDTO.timestamp,
//                    senderId = payload.memberId,
//                    chatId = payload.chatId,
//                    text = null
//                ) to emptyList()
//            }
//
//            MessageType.PIN -> {
//                val payload = messagesJsonFormatDTO.payload as PayLoadPinDTO
//                val message = Message(
//                    id = messagesJsonFormatDTO.messageId,
//                    type = messagesJsonFormatDTO.type,
//                    sendTime = messagesJsonFormatDTO.timestamp,
//                    senderId = payload.memberId,
//                    chatId = payload.chatId,
//                    text = null
//                )
//                val attachment = Attachment(
//                    id = payload.pinMessageId,
//                    messageId = message.id,
//                    type = AttachmentType.PIN
//                )
//                message to listOf(attachment)
//            }
//
//            MessageType.NEW_CHAT_AVATAR -> {
//                val payload = messagesJsonFormatDTO.payload as PayLoadNewChatAvatarDTO
//                val message = Message(
//                    id = messagesJsonFormatDTO.messageId,
//                    type = messagesJsonFormatDTO.type,
//                    sendTime = messagesJsonFormatDTO.timestamp,
//                    senderId = payload.memberId,
//                    chatId = payload.chatId,
//                    text = null
//                )
//                val attachment = Attachment(
//                    id = payload.avatarId,
//                    messageId = message.id,
//                    type = AttachmentType.NEW_CHAT_AVATAR
//                )
//                message to listOf(attachment)
//            }
        }

    fun convertToMessageByChat(message: Message): MessageByChat =
        MessageByChat(
            id = message.id,
            chatId = message.chatId,
            senderId = message.senderId,
            sendTime = message.sendTime,
            text = message.text,
            type = message.type
        )


    fun deserializeMessage(rs: Row): Message = kotlin.runCatching {
        Message(
            id = rs.getUuid("id") ?: throw NullCassandraFiledException("id"),
            type = MessageType.fromType(rs.getString("type")) ?: throw NullCassandraFiledException("type"),
            senderId = rs.getLong("sender_id"),
            chatId = rs.getLong("chat_id"),
            text = rs.getString("text"),
            sendTime = rs.get("send_time", LocalDateTime::class.java) ?: throw NullCassandraFiledException("send_time"),
            markedToDelete = rs.getBoolean("marked_to_delete")
        )
    }.getOrElse { e ->
        when (e) {
            is NullCassandraFiledException -> throw e
            else -> throw IllegalStateException("Deserialization in table Message failed", e)
        }
    }

//    fun convertToMessageJsonFormatDTO(baseMessage: BaseMessage): MessagesJsonFormatDTO{
//        null
//    }
}