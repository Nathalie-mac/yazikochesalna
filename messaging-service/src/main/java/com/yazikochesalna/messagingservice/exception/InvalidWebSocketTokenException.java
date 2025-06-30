package com.yazikochesalna.messagingservice.exception;

public class InvalidWebSocketTokenException extends RuntimeException {
    public InvalidWebSocketTokenException() {
        super("an invalid token has been specified or its TTL has expired");
    }
}
