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

    @PostMapping("/create")
    public Playlist createPlaylist(
            @RequestParam Long userId,
            @RequestParam String name,
            @RequestParam(required = false) String description
    ) {
        return playlistService.createPlaylist(userId, name, description);
    }

    @GetMapping("/user/{userId}")
    public List<Playlist> getUserPlaylists(@PathVariable Long userId) {
        return playlistService.getUserPlaylists(userId);
    }

    @PostMapping("/{playlistId}/add-song")
    public Playlist addSong(@PathVariable Long playlistId, @RequestParam Long songId) {
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @PostMapping("/{playlistId}/remove-song")
    public Playlist removeSong(@PathVariable Long playlistId, @RequestParam Long songId) {
        return playlistService.removeSongFromPlaylist(playlistId, songId);
    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }
}
