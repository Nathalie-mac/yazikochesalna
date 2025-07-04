package com.yazikochesalna.messagingservice.exception;

public class ReceiveSendResponseCustomException extends RuntimeException {
    public ReceiveSendResponseCustomException() {
        super("Error when sending a response to sending a message");
    }
}
