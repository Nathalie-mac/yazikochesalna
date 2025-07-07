package com.yazikochesalna.userservice.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class CustomErrorResponse {
    public String message;
    public Map<String, Object> info;

}
