package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadMessageDTO
import com.yazikochesalna.messagestorageservice.dto.PayLoadNoticeDTO
import com.yazikochesalna.messagestorageservice.dto.mapper.KafkaToMessageJsonFormatDTOConverter
import com.yazikochesalna.messagestorageservice.dto.serializers.MessageJsonFormatDeserializer
import com.yazikochesalna.messagestorageservice.exception.ErrorInMessageTypeException
import com.yazikochesalna.messagestorageservice.exception.ErrorKafkaDeserializatonException
import com.yazikochesalna.messagestorageservice.model.enums.MessageType
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private const val TOPIC_NAME: String = "messages"
@Service
class KafkaConsumerService(private val messageService: MessageService) {
    private val kafkaMessageConverter = KafkaToMessageJsonFormatDTOConverter()
    private val messageJsonFormatDeserializer = MessageJsonFormatDeserializer()
    private val log: Logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)
    private val mapper = ObjectMapper().apply {registerModule(JavaTimeModule())}



    @KafkaListener(topics = [TOPIC_NAME])
    fun consume(records: List<ConsumerRecord<String, String>>, ack: Acknowledgment) {
        if (records.isEmpty()) {
            log.info("Pupupuuu No messages in topic: '$TOPIC_NAME'")
            return
        }

        log.info("=== Received ${records.size} messages ===")
//        records.forEach { record ->
//            log.info("Received message: ${kafkaMessageConverter.
//            convertToMessageJsonFormatDTO(record.value())}")
//
//        }
        val messages = records.mapNotNull { record ->
//            kotlin.runCatching {
//                val dto = mapper.readValue(record.value(), MessagesJsonFormatDTO::class.java)
//                log.debug("Чето смапили: ${record.value()}")
//                return MessagesJsonFormatDTO(UUID.randomUUID(), MessageType.MESSAGE, LocalDateTime.now(), PayLoadMessageDTO(1, 1, "gegege"))
            MessagesJsonFormatDTO(UUID.randomUUID(), MessageType.MESSAGE, LocalDateTime.now(), PayLoadMessageDTO(1, 1, "gegege"))
        }

        messageService.saveMessagesBatch(messages).subscribe(
            null,
            {
                error -> log.error("Error while saving messages in DB")
            },
            {
                log.info("Messages saved successfully")
            }
        )

        ack.acknowledge()
        log.info("✔ Committed offsets up to ${records.last().offset()}")
    }
//    fun parseMessage(value: String): MessagesJsonFormatDTO {
//        //val mapper = jp.codec as ObjectMapper
//        val node: JsonNode = mapper.readTree(value)
//
//        return kotlin.runCatching {
//            val type = getMessageType(node)
//            MessagesJsonFormatDTO(
//                messageId = UUID.fromString(node["messageId"].asText()),
//                type,
//                timestamp = convertToLDT(node["timestamp"].asDouble()),
//                payload = getPayLoad(mapper, type, node)
//            )
//        }.getOrElse { e ->
//            when (e) {
//                is ErrorInMessageTypeException -> throw e
//                else -> throw ErrorKafkaDeserializatonException("deserialization")
//            }
//        }
////        val type = getMessageType(node)
////        val payload = getPayLoad(mapper, type, node)
////        val timestamp = convertToLDT(node["timestamp"].asDouble())
////        val messageId = UUID.fromString(node["messageId"].asText())
////        val messagesJsonFormatDTO = MessagesJsonFormatDTO(messageId, type, timestamp, payload)
////        return messagesJsonFormatDTO
//    }

//    private fun convertToLDT(instantStr: Double): LocalDateTime {
//        val seconds = instantStr.toLong()
//        val nanos = ((instantStr - seconds) * 1_000_000_000).toLong()
//        return Instant.ofEpochSecond(seconds, nanos)
//            .atOffset(ZoneOffset.UTC)
//            .toLocalDateTime()
//    }
//
//    private fun getMessageType(node: JsonNode): MessageType {
//        val typeNode = node["type"]
//            ?: throw ErrorInMessageTypeException("Message type cannot be null")
//        val typeStr = typeNode.asText()
//        return try {
//            MessageType.fromType(typeStr)
//        } catch (e: Exception) {
//            throw ErrorInMessageTypeException("Unknown message type: '$typeStr'")
//        }
//    }
//
//    private fun getPayLoad(mapper: ObjectMapper, messageType: MessageType, node: JsonNode): PayLoadDTO {
//        val payloadNode = node["payload"]
//        val payload: PayLoadDTO
//        if (messageType == MessageType.MESSAGE) {
//            payload = mapper.treeToValue<PayLoadMessageDTO>(payloadNode, PayLoadMessageDTO::class.java)
//        } else if (messageType == MessageType.NEW_MEMBER || messageType == MessageType.DROP_MEMBER) {
//            payload = mapper.treeToValue<PayLoadNoticeDTO>(payloadNode, PayLoadNoticeDTO::class.java)
//        } else {
//            throw ErrorKafkaDeserializatonException("type")
//        }
//        return payload
//    }


}