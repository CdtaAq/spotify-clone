package com.example.music.repository;

import com.example.music.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Search by album title (case-insensitive, partial match)
    List<Album> findByTitleContainingIgnoreCase(String title);

    // Optional: Filter albums by release year
    List<Album> findByReleaseYear(int year);
}
