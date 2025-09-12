package com.example.music.service;

import com.example.music.model.RefreshToken;
import com.example.music.model.User;
import com.example.music.repository.RefreshTokenRepository;
import com.example.music.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenPair createTokenPair(User user) {
        String access = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String refresh = jwtUtil.generateRefreshToken(user.getUsername());

        // persist refresh token with expiry taken from token's claims
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUser(user);
        // compute expiry instant (parse token)
        Instant expiry = jwtUtil.validateToken(refresh).getBody().getExpiration().toInstant();
        rt.setExpiryDate(expiry);
        refreshTokenRepository.save(rt);

        return new TokenPair(access, refresh);
    }

    public void revokeUserRefreshTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> refreshTokenRepository.delete(rt));
    }

    public static class TokenPair {
        public final String accessToken;
        public final String refreshToken;
        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
