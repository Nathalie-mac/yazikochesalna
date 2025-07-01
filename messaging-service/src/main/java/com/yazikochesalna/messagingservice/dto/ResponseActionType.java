package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public enum ResponseActionType {
    SEND_RESPONSE("send_response");

    private final String responseAction;

    ResponseActionType(String responseAction) {
        this.responseAction = responseAction;
    }
}
