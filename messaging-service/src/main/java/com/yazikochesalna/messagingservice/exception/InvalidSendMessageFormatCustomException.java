package com.yazikochesalna.messagingservice.exception;

public class InvalidSendMessageFormatCustomException extends RuntimeException {

    public InvalidSendMessageFormatCustomException() {
        super("Invalid JSON format");
    }
}
