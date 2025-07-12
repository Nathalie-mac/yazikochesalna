package com.yazikochesalna.fileservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MinioServerCustomException extends RuntimeException {
    public MinioServerCustomException(String message) {
        super(message);
    }
}
