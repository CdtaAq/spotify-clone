package com.example.music.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessMillis;
    private final long refreshMillis;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expires-minutes}") long accessMinutes,
            @Value("${app.jwt.refresh-token-expires-days}") long refreshDays
    ) {
        // secret must be long-enough for HS256; Keys.hmacShaKeyFor handles bytes
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessMillis = Duration.ofMinutes(accessMinutes).toMillis();
        this.refreshMillis = Duration.ofDays(refreshDays).toMillis();
    }

    public String generateAccessToken(String username, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(accessMillis)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("typ", "refresh")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshMillis)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        Object r = validateToken(token).getBody().get("role");
        return r == null ? null : r.toString();
    }

    public Date getExpirationFromToken(String token) {
        return validateToken(token).getBody().getExpiration();
    }
}
