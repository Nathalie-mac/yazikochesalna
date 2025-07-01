package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class SendResponseMessageDTO extends MessageDTO {

    private SendResponseResultType result;

    public SendResponseMessageDTO(Long requestId, SendResponseResultType result) {
        super("send_response", requestId);
        this.result = result;
    }
}