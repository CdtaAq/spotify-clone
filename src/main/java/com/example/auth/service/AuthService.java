package com.example.auth.service;

import com.example.auth.dto.AuthDtos.*;
import com.example.auth.model.User;
import com.example.auth.repo.UserRepository;
import com.example.auth.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = new User();
        u.setEmail(req.email);
        u.setPassword(encoder.encode(req.password));
        u.setDisplayName(req.displayName);
        userRepo.save(u);

        String access = jwtUtil.generateAccessToken(u.getEmail());
        String refresh = jwtUtil.generateRefreshToken(u.getEmail());
        AuthResponse resp = new AuthResponse();
        resp.accessToken = access;
        resp.refreshToken = refresh;
        resp.expiresInSeconds = 60 * 60; // match application.yml access-expiration-minutes
        resp.email = u.getEmail();
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email, req.password)
        );
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }
        String access = jwtUtil.generateAccessToken(req.email);
        String refresh = jwtUtil.generateRefreshToken(req.email);
        AuthResponse resp = new AuthResponse();
        resp.accessToken = access;
        resp.refreshToken = refresh;
        resp.expiresInSeconds = 60 * 60;
        resp.email = req.email;
        return resp;
    }
}
