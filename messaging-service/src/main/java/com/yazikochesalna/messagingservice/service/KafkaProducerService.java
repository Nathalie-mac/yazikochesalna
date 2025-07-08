package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC_NAME = "messages";
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    public void sendMessage(MessageDTO message, BiConsumer<SendResult<String, MessageDTO>, Throwable> callback) {
        kafkaTemplate.send(TOPIC_NAME, message).whenComplete(callback);
    }

    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
