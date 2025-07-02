package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;
import com.yazikochesalna.messagingservice.service.SendMessageToStorageService;
import org.springframework.stereotype.Service;

@Service
public class KafkaSendMessageToStorageService implements SendMessageToStorageService {
    @Override
    public void sendMessageToStorage(MessageToStorageDTO message) {

    }
}
