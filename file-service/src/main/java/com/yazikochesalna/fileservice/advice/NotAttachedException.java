package com.yazikochesalna.fileservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotAttachedException extends RuntimeException {
    public NotAttachedException(String message) {
        super(message);
    }
}
