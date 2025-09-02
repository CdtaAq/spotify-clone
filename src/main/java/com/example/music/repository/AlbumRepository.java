package com.example.music.repository;

import com.example.music.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Album findByTitle(String title);
    List<Album> findByPopularityScoreLessThan(int popularityScore);
}

