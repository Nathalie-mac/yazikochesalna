package com.yazikochesalna.userservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsCustomException extends RuntimeException {
    public UserAlreadyExistsCustomException(String message) {
        super(message);
    }
}
