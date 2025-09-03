package com.example.music.controller;

import com.example.music.model.Favorite;
import com.example.music.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{userId}")
    public Favorite addFavorite(@PathVariable Long userId,
                                @RequestParam(required = false) Long songId,
                                @RequestParam(required = false) Long albumId) {
        return favoriteService.addFavorite(userId, songId, albumId);
    }

    @GetMapping("/{userId}")
    public List<Favorite> getUserFavorites(@PathVariable Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    @DeleteMapping("/{favoriteId}")
    public void removeFavorite(@PathVariable Long favoriteId) {
        favoriteService.removeFavorite(favoriteId);
    }
}
