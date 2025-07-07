package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC_NAME = "messages";
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
