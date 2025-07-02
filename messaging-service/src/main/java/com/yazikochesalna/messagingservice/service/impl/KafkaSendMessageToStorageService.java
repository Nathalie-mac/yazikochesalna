package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.callback.MessageToStorageCallback;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.service.SendMessageToStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSendMessageToStorageService implements SendMessageToStorageService {
    private static final String TOPIC_NAME = "messages_to_storage";
    private final KafkaTemplate<String, MessageToStorageDTO> kafkaTemplate;

    @Override
    public void sendMessageToStorage(MessageToStorageDTO message, MessageToStorageCallback callback) {
        kafkaTemplate.send(TOPIC_NAME, message)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        callback.onError();
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
