package com.yazikochesalna.messagingservice.controller;

import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import com.yazikochesalna.messagingservice.service.RedissonWebSocketTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ws")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ConnectionController {


    private final RedissonWebSocketTokenService tokenService;

    @PostMapping("/connect")
    @Operation(
            summary = "Получить токен для установления websocket соединения",
            description = "Возвращает токен, который надо указать в качестве query параметра " +
                    "при подключении по websocket"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Токен получен",
                    content = {@Content(
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<?> getWebSocketToken() {
        Long userId = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();

        String token = tokenService.generateToken(userId);

        return ResponseEntity.ok(Map.of("token", token));
    }
}