package com.yazikochesalna.chatservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ChatCreationException extends RuntimeException {
    public ChatCreationException() {
        super();
    }
    public ChatCreationException(String message) {
        super(message);
    }
}
