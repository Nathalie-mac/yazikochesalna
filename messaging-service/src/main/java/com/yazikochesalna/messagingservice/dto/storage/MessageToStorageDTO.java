package com.yazikochesalna.messagingservice.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class MessageToStorageDTO {
    private String messageId;
    private Long senderId;
    private Long chatId;
    private String text;
    private Instant timestamp;
}
