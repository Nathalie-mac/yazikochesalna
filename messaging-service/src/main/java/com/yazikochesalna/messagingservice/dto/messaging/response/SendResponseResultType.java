package com.yazikochesalna.messagingservice.dto.messaging.response;

public enum SendResponseResultType {
    OK,
    INVALID_FORMAT,
    NOT_ALLOWED,
    NOT_SENT_TO_STORAGE,
    CHAT_SERVICE_INTEGRATION_PROBLEM,
    ERROR
}
