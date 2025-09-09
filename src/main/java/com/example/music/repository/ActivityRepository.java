package com.example.music.repository;

import com.example.music.model.Activity;
import com.example.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserOrderByCreatedAtDesc(User user);
}
