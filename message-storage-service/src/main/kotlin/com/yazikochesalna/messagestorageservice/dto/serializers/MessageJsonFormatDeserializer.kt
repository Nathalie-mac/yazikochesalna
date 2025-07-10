package com.yazikochesalna.messagestorageservice.dto.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.exception.ErrorInMessageTypeException
import com.yazikochesalna.messagestorageservice.exception.ErrorKafkaDeserializatonException
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class MessageJsonFormatDeserializer: StdDeserializer<MessagesJsonFormatDTO> {

    constructor() : this(MessagesJsonFormatDTO::class.java)

    constructor(vc: Class<MessagesJsonFormatDTO>) : super(vc)

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): MessagesJsonFormatDTO {
        val mapper = jp.codec as ObjectMapper
        val node: JsonNode = mapper.readTree(jp)

        return kotlin.runCatching {
            val type = getMessageType(node)
            MessagesJsonFormatDTO(
                messageId = UUID.fromString(node["messageId"].asText()),
                type,
                timestamp = convertToLDT(node["timestamp"].asDouble()),
                payload = getPayLoad(mapper, type, node)
            )
        }.getOrElse { e ->
            when (e) {
                is ErrorInMessageTypeException -> throw e
                else -> throw ErrorKafkaDeserializatonException("deserialization")
            }
        }
//        val type = getMessageType(node)
//        val payload = getPayLoad(mapper, type, node)
//        val timestamp = convertToLDT(node["timestamp"].asDouble())
//        val messageId = UUID.fromString(node["messageId"].asText())
//        val messagesJsonFormatDTO = MessagesJsonFormatDTO(messageId, type, timestamp, payload)
//        return messagesJsonFormatDTO
    }

    private fun convertToLDT(instantStr: Double): LocalDateTime {
        val seconds = instantStr.toLong()
        val nanos = ((instantStr - seconds) * 1_000_000_000).toLong()
        return Instant.ofEpochSecond(seconds, nanos)
            .atOffset(ZoneOffset.UTC)
            .toLocalDateTime()
    }

    private fun getMessageType(node: JsonNode): MessageType {
        val typeNode = node["type"]
            ?: throw ErrorInMessageTypeException("Message type cannot be null")
        val typeStr = typeNode.asText()
        return try {
            MessageType.fromType(typeStr)
        } catch (e: Exception) {
            throw ErrorInMessageTypeException("Unknown message type: '$typeStr'")
        }
    }

    private fun getPayLoad(mapper: ObjectMapper, messageType: MessageType, node: JsonNode): PayLoadDTO {
        val payloadNode = node["payload"]
        val payload: PayLoadDTO
        if (messageType == MessageType.MESSAGE) {
            payload = mapper.treeToValue<PayLoadMessageDTO>(payloadNode, PayLoadMessageDTO::class.java)
        } else if (messageType == MessageType.NEW_MEMBER || messageType == MessageType.DROP_MEMBER) {
            payload = mapper.treeToValue<PayLoadNoticeDTO>(payloadNode, PayLoadNoticeDTO::class.java)
        } else {
            throw ErrorKafkaDeserializatonException("type")
        }
        return payload
    }

}