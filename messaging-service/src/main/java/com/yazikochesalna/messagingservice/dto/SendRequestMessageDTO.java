package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class SendRequestMessageDTO {
    private String action;
    private Long requestId;
    private Long chatId;
    private String message;


    public SendRequestMessageDTO(Long requestId, Long chatId, String message) {
        this.action = RequestActionType.SEND.getRequestAction();
        this.requestId = requestId;
        this.chatId = chatId;
        this.message = message;
    }
}