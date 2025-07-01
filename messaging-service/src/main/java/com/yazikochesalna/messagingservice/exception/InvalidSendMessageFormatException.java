package com.yazikochesalna.messagingservice.exception;

public class InvalidSendMessageFormatException extends RuntimeException {

    public InvalidSendMessageFormatException() {
        super("Invalid JSON format");
    }
}
