package com.yazikochesalna.messagestorageservice.dto

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.yazikochesalna.messagestorageservice.model.db.Message
import com.yazikochesalna.messagestorageservice.model.enums.MessageType


class MessagesJSONSerializer: JsonSerializer<Message>() {
    override fun serialize(value: Message, gen: JsonGenerator, serializers: SerializerProvider) {
        val dto = convertToMessagesJsonFormstDto(value)
        gen.writeObject(dto)
    }

    fun convertToMessagesJsonFormstDto(message: Message): MessagesJsonFormatDTO {
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

}