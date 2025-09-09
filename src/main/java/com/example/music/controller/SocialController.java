package com.example.music.controller;

import com.example.music.model.Follow;
import com.example.music.model.Activity;
import com.example.music.service.SocialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    @PostMapping("/follow/user")
    public Follow followUser(@RequestParam Long followerId, @RequestParam Long userId) {
        return socialService.followUser(followerId, userId);
    }

    @PostMapping("/follow/artist")
    public Follow followArtist(@RequestParam Long followerId, @RequestParam Long artistId) {
        return socialService.followArtist(followerId, artistId);
    }

    @PostMapping("/activity")
    public Activity logActivity(@RequestParam Long userId,
                                @RequestParam String action,
                                @RequestParam String details) {
        return socialService.logActivity(userId, action, details);
    }

    @GetMapping("/feed/{userId}")
    public List<Activity> getFeed(@PathVariable Long userId) {
        return socialService.getUserFeed(userId);
    }
}
