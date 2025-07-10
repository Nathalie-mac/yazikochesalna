package com.yazikochesalna.messagestorageservice.dto.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorInEnumException
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorKafkaDeserializatonException
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import com.yazikochesalna.messagestorageservice.service.ChatServiceClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Component
open class MessageJsonFormatDeserializer :  StdDeserializer<MessagesJsonFormatDTO> {

    private val log: Logger = LoggerFactory.getLogger(ChatServiceClient::class.java)

    constructor() : this(MessagesJsonFormatDTO::class.java)

    constructor(vc: Class<MessagesJsonFormatDTO>) : super(vc)

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): MessagesJsonFormatDTO {
//        val mapper = jp.codec as ObjectMapper
//        val node: JsonNode = mapper.readTree(jp)
//
//        val type = getMessageType(node)
//        val messageId = UUID.fromString(node["messageId"].asText())
//        val timestamp = convertToLDT(node["timestamp"].asDouble())
//        val payload = getPayLoad(mapper, type, node)
//        return MessagesJsonFormatDTO(messageId, type, timestamp, payload)
        return runCatching {
            val mapper = jp.codec as ObjectMapper
            val node : JsonNode = mapper.readTree(jp)

            val type = getMessageType(node)
            val messageId = UUID.fromString(node["messageId"].asText())
            val timestamp = convertToLDT(node["timestamp"].asDouble())
            val payload = getPayLoad(mapper, type, node)

            MessagesJsonFormatDTO(messageId, type, timestamp, payload)
        }.fold(
            onSuccess = { it },
            onFailure = { e ->throw e  }
        )
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
            ?: throw ErrorInEnumException("Message type cannot be null")
        val typeStr = typeNode.asText()
        return try {
            MessageType.fromType(typeStr)
        } catch (e: Exception) {
            throw ErrorInEnumException("Unknown message type: '$typeStr'")
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