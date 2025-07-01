package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class SendRequestMessageDTO extends MessageDTO {

    private Long chatId;
    private String message;


    public SendRequestMessageDTO(Long requestId, Long chatId, String message) {
        super("send", requestId);
        this.chatId = chatId;
        this.message = message;
    }
}