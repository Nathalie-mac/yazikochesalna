package com.yazikochesalna.messagingservice.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageToStorageDTO {
    private MessageType type;
    private PayloadMessageToStorageDTO message;

}
