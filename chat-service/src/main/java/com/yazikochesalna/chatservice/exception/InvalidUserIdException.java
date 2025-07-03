package com.yazikochesalna.chatservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidUserIdException extends RuntimeException {
    public InvalidUserIdException() {
        super("Invalid user id");
    }
}
