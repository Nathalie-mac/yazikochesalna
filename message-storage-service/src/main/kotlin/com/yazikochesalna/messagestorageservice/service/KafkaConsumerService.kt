package com.yazikochesalna.messagestorageservice.service

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

private const val TOPIC_NAME: String = "messages"
@Service
class KafkaConsumerService {
    @KafkaListener(topics = [TOPIC_NAME])
    fun consume(messagesJsonFormatDTO: MessagesJsonFormatDTO, ack: Acknowledgment) {

    }
}