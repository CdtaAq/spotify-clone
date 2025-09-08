package com.example.music.repository;

import com.example.music.model.PlaybackSession;
import com.example.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlaybackSessionRepository extends JpaRepository<PlaybackSession, Long> {
    Optional<PlaybackSession> findByUser(User user);
}
