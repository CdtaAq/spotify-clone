package com.example.music.controller;

import com.example.music.model.Artist;
import com.example.music.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Example: GET /api/recommendations/artist?name=Drake
    @GetMapping("/artist")
    public List<Artist> recommendObscureArtists(@RequestParam String name) {
        return recommendationService.recommendObscureArtists(name);
    }
}
