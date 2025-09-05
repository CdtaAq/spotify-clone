package com.example.music.repository;

import com.example.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // Search by song title (case-insensitive, partial match)
    List<Song> findByTitleContainingIgnoreCase(String title);

    // Top 10 most played songs (Trending)
    List<Song> findTop10ByOrderByPlayCountDesc();

    // Top 10 least played songs (Obscure)
    List<Song> findTop10ByOrderByPlayCountAsc();

    // Optional: Filter by genre
    List<Song> findByGenreContainingIgnoreCase(String genre);

    // Optional: Filter by release year
    List<Song> findByReleaseYear(int year);
}
