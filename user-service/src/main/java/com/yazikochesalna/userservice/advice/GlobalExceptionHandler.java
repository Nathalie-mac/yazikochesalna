package com.yazikochesalna.userservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("timestamp", LocalDateTime.now());
        details.put("path", request.getDescription(false).replace("uri=", ""));

        String message = ex.getMessage() != null ? ex.getMessage() : "Произошла ошибка";

        return new ResponseEntity<>(
                new CustomErrorResponse(message, details),
                getStatus(ex)
        );
    }

    private HttpStatus getStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}