package com.yazikochesalna.messagingservice.dto.kafka;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PayloadMessageDTO {
    private UUID messageId;
    private Long senderId;
    private Long chatId;
    private String text;
    private Instant timestamp;
}
