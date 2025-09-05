package com.example.music.repository;

import com.example.music.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // Search by artist name (case-insensitive, partial match)
    List<Artist> findByNameContainingIgnoreCase(String name);
}
