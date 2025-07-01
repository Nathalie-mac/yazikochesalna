package com.yazikochesalna.common.service;

import com.yazikochesalna.common.authentication.JwtAuthenticationToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @Value("${security.jwt.service_token_expiration:600000}") //10 minutes by default
    private long serviceTokenExpiration;

    private static final String USER_ID = "uid";
    private static final String ROLES = "roles";

    public static final String SERVICE_ROLE = "ROLE_SERVICE";

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(long userId, long expiryTime) {
        return generateToken(userId, expiryTime, List.of());
    }

    private String generateToken(long userId, long expiryTime, List<String> roles) {
        JwtBuilder builder = Jwts.builder().
                claim(USER_ID, userId)
                .claim(ROLES,  roles)
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

    public String generateServiceToken() {
        return generateToken(-1, serviceTokenExpiration, List.of(SERVICE_ROLE));
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
        return extractAllClaims(token).get(USER_ID, Long.class);
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> extractRoles(String token) {
        return extractAllClaims(token)
                .get(ROLES, List.class)
                .stream()
                .map((role) -> new SimpleGrantedAuthority((String) role))
                .toList();
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
        } catch (io.jsonwebtoken.JwtException e) {
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
        return new JwtAuthenticationToken(token, extractUserID(token), extractRoles(token));
    }
}
