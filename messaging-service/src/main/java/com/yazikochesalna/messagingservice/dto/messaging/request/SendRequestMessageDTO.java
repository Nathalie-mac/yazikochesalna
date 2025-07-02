package com.yazikochesalna.messagingservice.dto.messaging.request;

import lombok.Getter;

@Getter
public class SendRequestMessageDTO {
    private final String action;
    private final Long requestId;
    private final Long chatId;
    private final String message;


    public SendRequestMessageDTO(Long requestId, Long chatId, String message) {
        this.action = RequestActionType.SEND.getRequestAction();
        this.requestId = requestId;
        this.chatId = chatId;
        this.message = message;
    }
}