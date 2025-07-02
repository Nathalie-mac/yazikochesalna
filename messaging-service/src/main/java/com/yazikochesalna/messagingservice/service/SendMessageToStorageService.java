package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;

public interface SendMessageToStorageService {
    void sendMessageToStorage(MessageToStorageDTO message);
}
