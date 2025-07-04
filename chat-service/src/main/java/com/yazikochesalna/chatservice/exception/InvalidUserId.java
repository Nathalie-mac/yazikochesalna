package com.yazikochesalna.chatservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidUserId extends RuntimeException {
    public InvalidUserId() {
        super("Invalid user id");
    }
}
