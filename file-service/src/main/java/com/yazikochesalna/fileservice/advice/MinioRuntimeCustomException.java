package com.yazikochesalna.fileservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MinioRuntimeCustomException extends RuntimeException {
    public MinioRuntimeCustomException(String message) {
        super(message);
    }
}
