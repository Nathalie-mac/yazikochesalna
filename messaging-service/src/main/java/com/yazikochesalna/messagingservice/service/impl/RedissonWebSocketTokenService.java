package com.yazikochesalna.messagingservice.service.impl;

import com.yazikochesalna.messagingservice.exception.InvalidWebSocketTokenException;
import com.yazikochesalna.messagingservice.service.WebSocketTokenService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedissonWebSocketTokenService implements WebSocketTokenService {

    private static final String TOKEN_PREFIX = "ws_token:";
    private static final Duration TOKEN_EXPIRATION = Duration.ofMinutes(5);
    private final RedissonClient redissonClient;


    public String generateToken(Long userId) {


        String token = UUID.randomUUID().toString();

        RBucket<Long> bucket = redissonClient.getBucket(TOKEN_PREFIX + token);
        bucket.set(userId, TOKEN_EXPIRATION);
        return token;
    }

    public Long validateAndGetUserId(String token) {
        if (!validateToken(token)) {
            throw new InvalidWebSocketTokenException();
        }
        RBucket<Long> bucket = redissonClient.getBucket(TOKEN_PREFIX + token);
        return bucket.getAndDelete();
    }

    private boolean validateToken(String token) {
        RBucket<Long> bucket = redissonClient.getBucket(TOKEN_PREFIX + token);
        return bucket.isExists();
    }
}