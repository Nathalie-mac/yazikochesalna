package com.yazikochesalna.userservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationCustomException extends RuntimeException {
    public ValidationCustomException(String message) {
        super(message);
    }
}
