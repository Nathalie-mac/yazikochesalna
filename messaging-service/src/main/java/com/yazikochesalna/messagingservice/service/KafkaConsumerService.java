package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.events.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String MESSAGES_TOPIC_NAME = "messages";
    private static final String EVENTS_TOPIC_NAME = "events";

    private final WebSocketEventService webSocketEventService;

    @KafkaListener(topics = MESSAGES_TOPIC_NAME)
    public void listenMessage(EventDTO event, Acknowledgment ack) {
        webSocketEventService.broadcastMessageToParticipants(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = EVENTS_TOPIC_NAME)
    public void listenEvent(EventDTO event, Acknowledgment ack) {
        webSocketEventService.broadcastEventToParticipants(event);
        ack.acknowledge();
    }

}
