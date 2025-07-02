package com.yazikochesalna.messagingservice.dto.messaging.response;

import lombok.Getter;

@Getter
public class SendResponseMessageDTO {

    private final String action;
    private final Long requestId;
    private final SendResponseResultType result;

    public SendResponseMessageDTO(Long requestId, SendResponseResultType result) {
        this.action = ResponseActionType.SEND_RESPONSE.getResponseAction();
        this.requestId = requestId;
        this.result = result;
    }
}