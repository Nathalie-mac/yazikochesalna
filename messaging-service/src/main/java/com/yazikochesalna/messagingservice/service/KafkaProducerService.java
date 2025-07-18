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

    private static final String MESSAGES_TOPIC_NAME = "messages";
    private static final String EVENTS_TOPIC_NAME = "events";
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public void sendMessage(EventDTO event, BiConsumer<SendResult<String, EventDTO>, Throwable> callback) {
        kafkaTemplate.send(MESSAGES_TOPIC_NAME, event).whenComplete(callback);
    }

    public void sendMessage(EventDTO event) {
        kafkaTemplate.send(MESSAGES_TOPIC_NAME, event);
    }

    public void sendEvent(EventDTO event) {
        kafkaTemplate.send(EVENTS_TOPIC_NAME, event);
    }
}
