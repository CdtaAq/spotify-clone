// src/main/java/com/example/music/controller/RecommendationController.java
package com.example.music.controller;

import com.example.music.model.Artist;
import com.example.music.repository.ArtistRepository;
import com.example.music.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final ArtistRepository artistRepository;

    public RecommendationController(RecommendationService recommendationService,
                                    ArtistRepository artistRepository) {
        this.recommendationService = recommendationService;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/obscure/{artistId}")
    public List<Artist> recommendObscure(@PathVariable Long artistId) {
        Artist current = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        return recommendationService.recommendObscureArtists(current);
    }
}
