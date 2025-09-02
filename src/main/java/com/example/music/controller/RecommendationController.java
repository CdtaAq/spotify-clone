package com.example.music.controller;

import com.example.music.model.Artist;
import com.example.music.model.Song;
import com.example.music.model.Album;
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

    // GET /api/recommendations/artist?name=Drake
    @GetMapping("/artist")
    public List<Artist> recommendObscureArtists(@RequestParam String name) {
        return recommendationService.recommendObscureArtists(name);
    }

    // GET /api/recommendations/song?title=One Dance
    @GetMapping("/song")
    public List<Song> recommendObscureSongs(@RequestParam String title) {
        return recommendationService.recommendObscureSongs(title);
    }

    // GET /api/recommendations/album?title=Views
    @GetMapping("/album")
    public List<Album> recommendObscureAlbums(@RequestParam String title) {
        return recommendationService.recommendObscureAlbums(title);
    }
}
