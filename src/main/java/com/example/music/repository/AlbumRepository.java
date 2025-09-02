package com.example.music.repository;

import com.example.music.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Find albums less popular than a given score
    List<Album> findByPopularityScoreLessThan(int popularityScore);

    // Lookup by title
    Album findByTitle(String title);
}
