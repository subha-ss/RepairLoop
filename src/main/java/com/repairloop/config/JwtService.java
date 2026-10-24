
package com.repairloop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final long EXPIRATION_MILLIS =
            1000L * 60 * 60; // 1 hour

    private final SecretKey secretKey;

    public JwtService() {
        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be set and contain at least 32 characters."
            );
        }

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(
            Long userId,
            String email,
            String role) {

        Date now = new Date();
        Date expiration = new Date(
                now.getTime() + EXPIRATION_MILLIS
        );

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return extractAllClaims(token);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
