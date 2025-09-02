package com.example.music.service;

import com.example.music.model.Artist;
import com.example.music.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ArtistRepository artistRepository;

    public RecommendationService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Recommend obscure artists based on the artist being listened to.
     * It finds artists with lower popularity score compared to the given artist.
     */
    public List<Artist> recommendObscureArtists(String currentArtistName) {
        // Lookup current artist
        Artist current = artistRepository.findByName(currentArtistName);
        if (current == null) {
            throw new RuntimeException("Artist not found: " + currentArtistName);
        }

        // Get obscure artists (lower popularity score)
        List<Artist> obscureArtists = artistRepository
                .findByPopularityScoreLessThan(current.getPopularityScore());

        // Sort by lowest popularity first (truly obscure)
        return obscureArtists.stream()
                .sorted(Comparator.comparingInt(Artist::getPopularityScore))
                .limit(5) // return top 5 obscure suggestions
                .collect(Collectors.toList());
    }
}
