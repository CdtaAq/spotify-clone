package com.example.music.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "play_history")
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    private LocalDateTime playedAt;

    public PlayHistory() {}

    public PlayHistory(User user, Song song, LocalDateTime playedAt) {
        this.user = user;
        this.song = song;
        this.playedAt = playedAt;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Song getSong() { return song; }
    public void setSong(Song song) { this.song = song; }

    public LocalDateTime getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDateTime playedAt) { this.playedAt = playedAt; }
}
