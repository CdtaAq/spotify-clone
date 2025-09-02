// src/main/java/com/example/music/service/RecommendationService.java
package com.example.music.service;

import com.example.music.model.Artist;
import com.example.music.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final ArtistRepository artistRepository;

    public RecommendationService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> recommendObscureArtists(Artist currentArtist) {
        if (currentArtist.getGenres().isEmpty()) {
            return List.of(); // no genres, can’t recommend
        }
        List<Long> genreIds = currentArtist.getGenres()
                .stream()
                .map(g -> g.getId())
                .toList();

        List<Artist> candidates = artistRepository.findObscureArtistsByGenre(genreIds);

        // filter out the current artist itself
        return candidates.stream()
                .filter(a -> !a.getId().equals(currentArtist.getId()))
                .limit(5) // top 5 obscure recommendations
                .toList();
    }
}
