package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.events.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String TOPIC_NAME = "messages";

    private final WebSocketMessageService webSocketMessageService;

    @KafkaListener(topics = TOPIC_NAME)
    public void listenMessage(EventDTO event, Acknowledgment ack) {
        webSocketMessageService.broadcastMessageToParticipants(event);
        ack.acknowledge();
    }

}
