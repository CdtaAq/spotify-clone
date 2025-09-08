package com.example.music.service;

import com.example.music.model.*;
import com.example.music.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlaybackService {

    private final PlaybackSessionRepository playbackSessionRepository;
    private final PlayHistoryRepository playHistoryRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public PlaybackService(PlaybackSessionRepository playbackSessionRepository,
                           PlayHistoryRepository playHistoryRepository,
                           UserRepository userRepository,
                           SongRepository songRepository) {
        this.playbackSessionRepository = playbackSessionRepository;
        this.playHistoryRepository = playHistoryRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    public PlaybackSession playSong(Long userId, Long songId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // update song play count
        song.setPlayCount(song.getPlayCount() + 1);
        songRepository.save(song);

        // log play history
        playHistoryRepository.save(new PlayHistory(user, song, LocalDateTime.now()));

        // update playback session
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElse(new PlaybackSession(user, null, false, null));

        session.setCurrentSong(song);
        session.setPlaying(true);
        session.setStartedAt(LocalDateTime.now());

        return playbackSessionRepository.save
