// src/main/java/com/example/music/repository/ArtistRepository.java
package com.example.music.repository;

import com.example.music.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // Find artists in same genre(s), order by popularity ascending
    @Query("SELECT a FROM Artist a JOIN a.genres g WHERE g.id IN :genreIds ORDER BY a.popularityScore ASC")
    List<Artist> findObscureArtistsByGenre(List<Long> genreIds);
}
