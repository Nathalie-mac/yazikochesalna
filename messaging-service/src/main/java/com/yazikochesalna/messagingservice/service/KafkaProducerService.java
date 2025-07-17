package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.events.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC_NAME = "messages";
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public void sendMessage(EventDTO event, BiConsumer<SendResult<String, EventDTO>, Throwable> callback) {
        kafkaTemplate.send(TOPIC_NAME, event).whenComplete(callback);
    }

    public void sendMessage(EventDTO event) {
        kafkaTemplate.send(TOPIC_NAME, event);
    }
}
