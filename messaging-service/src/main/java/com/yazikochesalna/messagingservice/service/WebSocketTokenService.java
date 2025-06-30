package com.yazikochesalna.messagingservice.service;

public interface WebSocketTokenService {
    String generateToken(Long userId);

    Long validateAndGetUserId(String token);
}
