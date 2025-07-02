package com.yazikochesalna.messagingservice.exception;

public class ReceiveMessageException extends RuntimeException {
    public ReceiveMessageException() {
        super("Error when sending a message to client");
    }
}

