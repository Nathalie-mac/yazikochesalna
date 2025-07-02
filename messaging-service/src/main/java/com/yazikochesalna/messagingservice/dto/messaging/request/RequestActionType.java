package com.yazikochesalna.messagingservice.dto.messaging.request;

import lombok.Getter;

@Getter
public enum RequestActionType {
    SEND("send");
    private final String requestAction;

    RequestActionType(String requestAction) {
        this.requestAction = requestAction;
    }
}
