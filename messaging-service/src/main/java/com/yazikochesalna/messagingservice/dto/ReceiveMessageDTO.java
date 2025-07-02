package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class ReceiveMessageDTO {
    private String action = ReceiveActionType.NEW_MESSAGE.getAction();
    private Long chatId;
    private MessageDTO message;

    public ReceiveMessageDTO(Long chatId, MessageDTO message) {
        this.chatId = chatId;
        this.message = message;
    }
}
