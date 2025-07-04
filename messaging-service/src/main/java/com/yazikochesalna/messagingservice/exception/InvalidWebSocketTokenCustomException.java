package com.yazikochesalna.messagingservice.exception;

public class InvalidWebSocketTokenCustomException extends RuntimeException {
    public InvalidWebSocketTokenCustomException() {
        super("an invalid token has been specified or its TTL has expired");
    }
}
