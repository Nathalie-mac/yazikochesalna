package com.yazikochesalna.messagingservice.dto.messaging.notification;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageDTO {
    private UUID messageId;
    private Long senderId;
    private String text;
    private Instant timestamp;


}