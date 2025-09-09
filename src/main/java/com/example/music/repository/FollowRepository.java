package com.example.music.repository;

import com.example.music.model.Follow;
import com.example.music.model.User;
import com.example.music.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User user);
    List<Follow> findByFollowingUser(User user);
    List<Follow> findByFollowingArtist(Artist artist);
}
