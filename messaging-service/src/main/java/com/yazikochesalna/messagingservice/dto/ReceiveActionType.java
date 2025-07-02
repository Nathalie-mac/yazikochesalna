package com.yazikochesalna.messagingservice.dto;

import lombok.Getter;

@Getter
public enum ReceiveActionType {
    NEW_MESSAGE("new_message");
    private final String action;

    ReceiveActionType(String action) {
        this.action = action;
    }
}
