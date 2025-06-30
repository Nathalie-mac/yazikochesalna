package com.yazikochesalna.messagingservice.controller;

import com.yazikochesalna.messagingservice.service.WebSocketTokenService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> requestWebSocketConnection(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {

        //TODO:извлечение userID из JWT (валидация JWT в фильтре)
        Long userId = 1L;

        String token = tokenService.generateToken(userId);
        String host = request.getRemoteHost();
        String wsUrl = "ws://" + host + "/chat/connect?token=" + token;
        return ResponseEntity.ok(Map.of("wsUrl", wsUrl));
    }
}