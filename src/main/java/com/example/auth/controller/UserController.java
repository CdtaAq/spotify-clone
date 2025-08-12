package com.example.auth.controller;

import com.example.auth.model.User;
import com.example.auth.repo.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return repo.findByEmail(userDetails.getUsername()).orElseThrow();
    }
}
