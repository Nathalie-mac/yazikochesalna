package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

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

        messageService.saveMessagesBatch(records)
            .doOnSuccess {
                log.info("Successfully saved ${records.size} messages to DB")
                ack.acknowledge()
                log.info("Successfully committed offsets up to ${offsets.last()}")
            }
            .doOnError { error ->
                log.error("Failed to save messages to DB: ${error.message}", error)
            }
            .subscribe()
    }
}