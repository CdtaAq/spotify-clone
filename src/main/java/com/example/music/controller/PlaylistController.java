package com.example.music.controller;

import com.example.music.model.Playlist;
import com.example.music.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/{userId}")
    public Playlist createPlaylist(@PathVariable Long userId, @RequestParam String title) {
        return playlistService.createPlaylist(userId, title);
    }

    @GetMapping("/{userId}")
    public List<Playlist> getUserPlaylists(@PathVariable Long userId) {
        return playlistService.getUserPlaylists(userId);
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public Playlist addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @DeleteMapping("/{playlistId}/removeSong/{songId}")
    public Playlist removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        return playlistService.removeSongFromPlaylist(playlistId, songId);
    }
}
