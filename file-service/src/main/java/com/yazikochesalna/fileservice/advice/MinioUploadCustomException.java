package com.yazikochesalna.fileservice.advice;

import io.minio.errors.MinioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MinioUploadCustomException extends RuntimeException {
    public MinioUploadCustomException(String message) {
        super(message);
    }
}
