package com.example.music.service;

import com.example.music.model.*;
import com.example.music.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SocialService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final ActivityRepository activityRepository;

    public SocialService(FollowRepository followRepository,
                         UserRepository userRepository,
                         ArtistRepository artistRepository,
                         ActivityRepository activityRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.activityRepository = activityRepository;
    }

    public Follow followUser(Long followerId, Long userId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User followingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return followRepository.save(new Follow(follower, followingUser, null));
    }

    public Follow followArtist(Long followerId, Long artistId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        return followRepository.save(new Follow(follower, null, artist));
    }

    public Activity logActivity(Long userId, String action, String details) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Activity activity = new Activity(user, action, details, LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public List<Activity> getUserFeed(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return activityRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
