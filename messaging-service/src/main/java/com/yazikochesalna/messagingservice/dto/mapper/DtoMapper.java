package com.yazikochesalna.messagingservice.dto.mapper;

import com.yazikochesalna.messagingservice.dto.messaging.notification.MessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.storage.MessageToStorageDTO;

import java.time.Instant;
import java.util.UUID;

public class DtoMapper {
    public static MessageToStorageDTO toMessageToStorageDTO(SendRequestMessageDTO sendRequestMessageDTO, Long userId) {
        return new MessageToStorageDTO(UUID.randomUUID().toString(),
                userId,
                sendRequestMessageDTO.getChatId(),
                sendRequestMessageDTO.getMessage(),
                Instant.now());
    }

    public static ReceiveMessageDTO toReceiveMessageDTO(MessageToStorageDTO messageToStorageDTO) {
        MessageDTO messageDTO = new MessageDTO(messageToStorageDTO.getMessageId(),
                messageToStorageDTO.getSenderId(),
                messageToStorageDTO.getText(),
                messageToStorageDTO.getTimestamp());
        return new ReceiveMessageDTO(
                messageToStorageDTO.getChatId(),
                messageDTO);
    }
}
