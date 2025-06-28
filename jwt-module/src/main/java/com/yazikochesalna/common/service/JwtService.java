package com.yazikochesalna.common.service;

import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${security.jwt.secret_key}")
    private String secretKey;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private static final String USER_ID = "uid";
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(long userId, long expiryTime) {
        JwtBuilder builder = Jwts.builder().
                claim(USER_ID, userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSigningKey());

        return builder.compact();
    }

    public String generateAccessToken(long userId) {

        return generateToken(userId, accessTokenExpiration);
    }

    public String generateRefreshToken(long userId) {

        return generateToken(userId, refreshTokenExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    public long extractUserID(String token) {
        return (long) extractAllClaims(token).get(USER_ID);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean verifyToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (io.jsonwebtoken.JwtException) {
            return false;
        }
    }

    public boolean isValidAccess(String token) {
        return verifyToken(token);
    }

    public boolean isValidRefresh(String token) {

        return verifyToken(token);
    }

    public Authentication getAuthentication(String token) {
        return new JwtAuthenticationToken(token, extractUserID(token), List.of());
    }
}
