package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class SendResponseMessageDTO extends MessageDTO {

    private SendResponseResultType result;

    public SendResponseMessageDTO(Long requestId, SendResponseResultType result) {
        super(ResponseActionType.SEND_RESPONSE.getResponseAction(), requestId);
        this.result = result;
    }
}