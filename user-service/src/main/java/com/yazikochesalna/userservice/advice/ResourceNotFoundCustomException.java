package com.yazikochesalna.userservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundCustomException extends RuntimeException {
    public ResourceNotFoundCustomException(String message) {
        super(message);
    }
}


