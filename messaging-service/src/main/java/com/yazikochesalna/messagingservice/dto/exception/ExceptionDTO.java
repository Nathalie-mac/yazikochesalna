package com.yazikochesalna.messagingservice.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String message;
    private Map<String, Object> details;

}
