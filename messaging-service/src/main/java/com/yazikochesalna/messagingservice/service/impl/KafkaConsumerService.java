package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.dto.kafka.MessageDTO;
import com.yazikochesalna.messagingservice.dto.kafka.MessageType;
import com.yazikochesalna.messagingservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final String TOPIC_NAME = "messages";

    private final WebSocketService webSocketService;

    @KafkaListener(topics = TOPIC_NAME)
    public void listenMessage(MessageDTO message, Acknowledgment ack) {
        if (message.getType().equals(MessageType.MESSAGE)) {
            webSocketService.receiveMessagesToMembers(message);
            ack.acknowledge();
        }
    }

}
