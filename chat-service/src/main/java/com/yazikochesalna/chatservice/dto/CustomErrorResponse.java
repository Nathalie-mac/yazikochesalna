package com.yazikochesalna.chatservice.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;

import java.util.Map;

public class CustomErrorResponse {

    private final HttpStatusCode statusCode;
    private final String message;
    private final Map<String, String> details;

    public CustomErrorResponse(@NonNull HttpStatusCode statusCode) {
        this(statusCode, null);
    }

    public CustomErrorResponse(@NonNull HttpStatusCode statusCode, String message) {
        this(statusCode, message, null);
    }

    public CustomErrorResponse(@NonNull HttpStatusCode statusCode, String message, Map<String, String> details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

    @NonNull
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        if (this.message == null) {
            HttpStatus httpStatus = HttpStatus.resolve(this.statusCode.value());
            if (httpStatus != null) {
                return httpStatus.getReasonPhrase();
            }
            return this.statusCode.toString();
        }
        return this.message;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
