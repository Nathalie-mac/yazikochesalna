package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public class SendResponseMessageDTO {

    private String action;
    private Long requestId;
    private SendResponseResultType result;

    public SendResponseMessageDTO(Long requestId, SendResponseResultType result) {
        this.action = ResponseActionType.SEND_RESPONSE.getResponseAction();
        this.requestId = requestId;
        this.result = result;
    }
}