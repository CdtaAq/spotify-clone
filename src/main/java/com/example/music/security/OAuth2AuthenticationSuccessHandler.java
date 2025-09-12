package com.example.music.security;

import com.example.music.model.User;
import com.example.music.model.Role;
import com.example.music.repository.UserRepository;
import com.example.music.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final String redirectUri;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository,
                                              TokenService tokenService,
                                              JwtUtil jwtUtil,
                                              @org.springframework.beans.factory.annotation.Value("${app.oauth2.authorized-redirect-uri}") String redirectUri) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();

        // extract a username/email depending on provider
        String email = null;
        if (attributes.containsKey("email")) {
            email = attributes.get("email").toString();
        } else if (attributes.containsKey("login")) {
            // github uses 'login' as username; could call user/email endpoint for email
            email = attributes.get("login").toString() + "@github.local";
        }

        if (!StringUtils.hasText(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "OAuth2 provider did not return email");
            return;
        }

        // create or load user
        Optional<User> uopt = userRepository.findByUsername(email);
        User user;
        if (uopt.isPresent()) {
            user = uopt.get();
        } else {
            user = new User();
            user.setUsername(email);
            user.setPassword("oauth2user"); // placeholder; users login with provider
            user.setRole(Role.USER);
            userRepository.save(user);
        }

        // create tokens
        var tokenPair = tokenService.createTokenPair(user);

        // redirect to front-end app carrying tokens (fragment or query param)
        String redirect = redirectUri +
                "?accessToken=" + URLEncoder.encode(tokenPair.accessToken, StandardCharsets.UTF_8) +
                "&refreshToken=" + URLEncoder.encode(tokenPair.refreshToken, StandardCharsets.UTF_8);

        response.sendRedirect(redirect);
    }
}
