package com.yazikochesalna.messagestorageservice.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.time.Instant

private const val TOPIC_NAME: String = "messages"
@Service
class KafkaConsumerService {
    private val log: Logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)

    @KafkaListener(topics = [TOPIC_NAME],
        //containerFactory = "kafkaListenerContainerFactory")
    )
    fun consume(records: List<ConsumerRecord<String, String>>, ack: Acknowledgment) {
        if (records.isEmpty()) {
            log.warn("⚠️ No messages in topic: '$TOPIC_NAME'")
            return
        }

        log.info("=== Received ${records.size} messages ===")
        records.forEach { record ->
            log.info("""
            Offset: ${record.offset()}
            Partition: ${record.partition()}
            Key: ${record.key()}
            Value: ${record.value()}
            Timestamp: ${Instant.ofEpochMilli(record.timestamp())}
        """.trimIndent())

            // Ваша бизнес-логика здесь
        }

        ack.acknowledge()
        log.info("✔ Committed offsets up to ${records.last().offset()}")
    }
}