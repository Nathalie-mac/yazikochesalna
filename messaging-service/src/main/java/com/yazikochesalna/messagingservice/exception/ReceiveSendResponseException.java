package com.yazikochesalna.messagingservice.exception;

public class ReceiveSendResponseException extends RuntimeException {
    public ReceiveSendResponseException() {
        super("Error when sending a response to sending a message");
    }
}
