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
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private const val TOPIC_NAME: String = "messages"
@Service
class KafkaConsumerService(private val messageService: MessageService) {
    //private val kafkaMessageConverter = KafkaToMessageJsonFormatDTOConverter()
    //private val messageJsonFormatDeserializer = MessageJsonFormatDeserializer()
    private val log: Logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)
    //private val mapper = ObjectMapper().apply {registerModule(JavaTimeModule())}



    @KafkaListener(topics = [TOPIC_NAME],
        )
    fun consume(records: List<MessagesJsonFormatDTO>, ack: Acknowledgment,
                @Header(KafkaHeaders.OFFSET) offsets: List<Long>) {
        if (records.isEmpty()) {
            log.info("No messages in topic: '$TOPIC_NAME'")
            return
        }

        log.info("=== Received ${records.size} messages ===")
        records.forEach {
            log.info("type: ${it.type}, UUID: ${it.messageId}")
        }
        ack.acknowledge()
        log.info("Susseccfully Committed offsets up to ${offsets.last()}")
    }
}