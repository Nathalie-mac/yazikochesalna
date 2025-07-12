package com.yazikochesalna.fileservice.advice;

import io.minio.errors.MinioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MinioFileNotFoundCustomException extends RuntimeException {
    public MinioFileNotFoundCustomException(String message) {
        super(message);
    }
}


