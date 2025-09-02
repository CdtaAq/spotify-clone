package com.example.music.repository;

import com.example.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Song findByTitle(String title);
    List<Song> findByPopularityScoreLessThan(int popularityScore);
}
