package com.yazikochesalna.messagingservice.controller;

import com.yazikochesalna.messagingservice.service.WebSocketTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {


    private final WebSocketTokenService tokenService;

    @GetMapping("/connect")
    public ResponseEntity<?> requestWebSocketConnection(@RequestHeader("Authorization") String authHeader) {

        //TODO:извлечение userID из JWT (валидация JWT в фильтре)
        Long userId = 1L;

        String token = tokenService.generateToken(userId);
        return ResponseEntity.ok(Map.of("token", token));
    }
}