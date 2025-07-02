package com.yazikochesalna.messagingservice.dto.messaging.notification;

import lombok.Getter;

@Getter
public class ReceiveMessageDTO {
    private final String action = ReceiveActionType.NEW_MESSAGE.getAction();
    private final Long chatId;
    private final MessageDTO message;

    public ReceiveMessageDTO(Long chatId, MessageDTO message) {
        this.chatId = chatId;
        this.message = message;
    }
}
