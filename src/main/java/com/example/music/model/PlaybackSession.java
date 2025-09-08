package com.example.music.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playback_sessions")
public class PlaybackSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song currentSong;

    private boolean playing;
    private LocalDateTime startedAt;

    public PlaybackSession() {}

    public PlaybackSession(User user, Song currentSong, boolean playing, LocalDateTime startedAt) {
        this.user = user;
        this.currentSong = currentSong;
        this.playing = playing;
        this.startedAt = startedAt;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Song getCurrentSong() { return currentSong; }
    public void setCurrentSong(Song currentSong) { this.currentSong = currentSong; }

    public boolean isPlaying() { return playing; }
    public void setPlaying(boolean playing) { this.playing = playing; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
}
