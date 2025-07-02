package com.yazikochesalna.messagingservice.dto.messaging.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class MessageDTO {
    private String messageId;
    private Long senderId;
    private String text;
    private Instant timestamp;


}