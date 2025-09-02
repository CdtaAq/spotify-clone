package com.example.music.repository;

import com.example.music.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    // Find artists less popular than a given score
    List<Artist> findByPopularityScoreLessThan(int popularityScore);

    // Find artists with a specific name (useful for lookup)
    Artist findByName(String name);
}
