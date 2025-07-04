package com.yazikochesalna.messagingservice.dto.messaging.notification;

import lombok.Getter;

@Getter
public enum ActionType {
    NEW_MESSAGE,
    SEND,
    SEND_RESPONSE;
}
