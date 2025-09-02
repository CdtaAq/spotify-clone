package com.example.music.service;

import com.example.music.model.Playlist;
import com.example.music.model.Song;
import com.example.music.model.User;
import com.example.music.repository.PlaylistRepository;
import com.example.music.repository.SongRepository;
import com.example.music.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepo;
    private final SongRepository songRepo;
    private final UserRepository userRepo;

    public PlaylistService(PlaylistRepository playlistRepo, SongRepository songRepo, UserRepository userRepo) {
        this.playlistRepo = playlistRepo;
        this.songRepo = songRepo;
        this.userRepo = userRepo;
    }

    public Playlist createPlaylist(Long userId, String name, String description) {
        User owner = userRepo.findById(userId).orElseThrow();
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setOwner(owner);
        return playlistRepo.save(playlist);
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepo.findByOwnerId(userId);
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepo.findById(playlistId).orElseThrow();
        Song song = songRepo.findById(songId).orElseThrow();
        playlist.getSongs().add(song);
        return playlistRepo.save(playlist);
    }

    public Playlist removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepo.findById(playlistId).orElseThrow();
        Song song = songRepo.findById(songId).orElseThrow();
        playlist.getSongs().remove(song);
        return playlistRepo.save(playlist);
    }

    public void deletePlaylist(Long playlistId) {
        playlistRepo.deleteById(playlistId);
    }
}
