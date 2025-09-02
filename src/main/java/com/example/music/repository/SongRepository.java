package com.example.music.repository;

import com.example.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // Find songs less popular than a given score
    List<Song> findByPopularityScoreLessThan(int popularityScore);

    // Lookup by title
    Song findByTitle(String title);
}
