package com.yazikochesalna.userservice.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Data
public class CustomErrorResponse {
    public String message;
    public Map<String, Object> info;

}
