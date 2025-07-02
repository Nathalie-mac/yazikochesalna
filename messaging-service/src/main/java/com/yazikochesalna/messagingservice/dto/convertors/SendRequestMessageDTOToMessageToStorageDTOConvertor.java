package com.yazikochesalna.messagingservice.dto.convertors;

import com.yazikochesalna.messagingservice.dto.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;

import java.time.Instant;
import java.util.UUID;

public class SendRequestMessageDTOToMessageToStorageDTOConvertor {
    public static MessageToStorageDTO convert(SendRequestMessageDTO sendRequestMessageDTO, Long userId) {
        return new MessageToStorageDTO(UUID.randomUUID().toString(),
                userId,
                sendRequestMessageDTO.getChatId(),
                sendRequestMessageDTO.getMessage(),
                Instant.now());
    }
}
