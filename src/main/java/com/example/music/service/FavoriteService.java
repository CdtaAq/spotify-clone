package com.example.music.service;

import com.example.music.model.Favorite;
import com.example.music.model.User;
import com.example.music.repository.FavoriteRepository;
import com.example.music.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public Favorite addFavorite(Long userId, Long songId, Long albumId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Favorite favorite = new Favorite(user, songId, albumId);
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
