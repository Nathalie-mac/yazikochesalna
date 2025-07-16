package com.yazikochesalna.messagestorageservice.model.db

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import org.springframework.stereotype.Component

@Component
class CassandraEntitiesConvertor {
    fun convertToMessagesJsonFormatDto(message: Message): MessagesJsonFormatDTO {
        return MessagesJsonFormatDTO(
            messageId = message.id,
            type = message.type,
            timestamp = message.sendTime,
            payload = when (message.type){
                MessageType.MESSAGE -> PayLoadMessageDTO(
                    senderId = message.senderId,
                    chatId = message.chatId,
                    text = message.text?: ""
                )
                MessageType.NEW_MEMBER,
                MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
                    memberId = message.senderId,
                    chatId = message.chatId,
                )
                else -> throw IllegalArgumentException("Not supported message type ${message.type}")
            }
        )
    }

    fun convertToMessagesJsonFormatDto(messageByChat: MessageByChat): MessagesJsonFormatDTO {
        return MessagesJsonFormatDTO(
            messageId = messageByChat.id,
            type = messageByChat.type,
            timestamp = messageByChat.sendTime,
            payload = when (messageByChat.type){
                MessageType.MESSAGE -> PayLoadMessageDTO(
                    senderId = messageByChat.senderId,
                    chatId = messageByChat.chatId,
                    text = messageByChat.text?: ""
                )
                MessageType.NEW_MEMBER,
                MessageType.DROP_MEMBER -> PayLoadNoticeDTO(
                    memberId = messageByChat.senderId,
                    chatId = messageByChat.chatId,
                )
                else -> throw IllegalArgumentException("Not supported message type ${messageByChat.type}")
            }
        )
    }

    fun convertToMessage(messagesJsonFormatDTO: MessagesJsonFormatDTO): Message {
        return when (messagesJsonFormatDTO.type) {
            MessageType.MESSAGE -> {
                val payload = messagesJsonFormatDTO.payload as PayLoadMessageDTO
                Message(
                    id = messagesJsonFormatDTO.messageId,
                    type = messagesJsonFormatDTO.type,
                    sendTime = messagesJsonFormatDTO.timestamp,
                    senderId = payload.senderId,
                    chatId = payload.chatId,
                    text = payload.text
                )
            }
            MessageType.NEW_MEMBER, MessageType.DROP_MEMBER -> {
                val payload = messagesJsonFormatDTO.payload as PayLoadNoticeDTO
                Message(
                    id = messagesJsonFormatDTO.messageId,
                    type = messagesJsonFormatDTO.type,
                    sendTime = messagesJsonFormatDTO.timestamp,
                    senderId = payload.memberId,
                    chatId = payload.chatId,
                    text = null
                )
            }
            else -> throw ErrorInEnumException("Not supported message type $messagesJsonFormatDTO.type")
        }
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
}