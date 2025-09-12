package com.example.music.controller;

import com.example.music.dto.AuthRequest;
import com.example.music.dto.AuthResponse;
import com.example.music.model.User;
import com.example.music.model.Role;
import com.example.music.repository.UserRepository;
import com.example.music.service.TokenService;
import com.example.music.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_exists"));
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(Role.USER);
        userRepository.save(u);

        var tokens = tokenService.createTokenPair(u);
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken, tokens.refreshToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Optional<User> ou = userRepository.findByUsername(req.getUsername());
        if (ou.isEmpty() || !passwordEncoder.matches(req.getPassword(), ou.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
        User user = ou.get();
        var tokens = tokenService.createTokenPair(user);
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken, tokens.refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_refresh_token"));
        }
        // validate signature and presence in DB
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            // ensure refresh token exists and not expired
            var opt = tokenService.refreshTokenRepository.findByToken(refreshToken);
            if (opt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "invalid_refresh_token"));
            }
            var rt = opt.get();
            if (rt.getExpiryDate().isBefore(Instant.now())) {
                tokenService.revokeRefreshToken(refreshToken);
                return ResponseEntity.status(401).body(Map.of("error", "refresh_token_expired"));
            }
            // ok — issue new access token (and optionally rotate refresh token)
            var userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) return ResponseEntity.status(401).body(Map.of("error","user_not_found"));
            User user = userOpt.get();
            String access = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());

            // Optional: rotate refresh token — create new one, delete old
            tokenService.revokeRefreshToken(refreshToken);
            String newRefresh = jwtUtil.generateRefreshToken(user.getUsername());
            RefreshToken newRt = new RefreshToken();
            newRt.setToken(newRefresh);
            newRt.setUser(user);
            newRt.setExpiryDate(jwtUtil.validateToken(newRefresh).getBody().getExpiration().toInstant());
            tokenService.refreshTokenRepository.save(newRt);

            return ResponseEntity.ok(new AuthResponse(access, newRefresh));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_refresh_token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        // Revoke refresh token (if provided) or revoke all refresh tokens for a user
        String refreshToken = body.get("refreshToken");
        if (refreshToken != null) {
            tokenService.revokeRefreshToken(refreshToken);
            return ResponseEntity.ok(Map.of("message", "logged_out"));
        }
        String username = body.get("username");
        if (username != null) {
            userRepository.findByUsername(username).ifPresent(tokenService::revokeUserRefreshTokens);
            return ResponseEntity.ok(Map.of("message", "logged_out_all"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "missing_info"));
    }
}
