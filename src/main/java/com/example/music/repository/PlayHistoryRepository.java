package com.example.music.repository;

import com.example.music.model.PlayHistory;
import com.example.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    List<PlayHistory> findByUserOrderByPlayedAtDesc(User user);
}
