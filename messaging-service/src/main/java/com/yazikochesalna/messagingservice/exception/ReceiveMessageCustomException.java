package com.yazikochesalna.messagingservice.exception;

public class ReceiveMessageCustomException extends RuntimeException {
    public ReceiveMessageCustomException() {
        super("Error when sending a message to client");
    }
}

