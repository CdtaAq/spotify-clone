package com.example.music.controller;

import com.example.music.dto.AlbumDto;
import com.example.music.dto.ArtistDto;
import com.example.music.dto.SongDto;
import com.example.music.model.Album;
import com.example.music.model.Artist;
import com.example.music.model.Song;
import com.example.music.service.MusicService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final MusicService musicService;

    public AdminController(MusicService musicService) {
        this.musicService = musicService;
    }

    // create artist
    @PostMapping("/artists")
    public ResponseEntity<ArtistDto> createArtist(@RequestParam @NotBlank String name,
                                                  @RequestParam(required = false) String bio) {
        Artist a = musicService.createArtist(name, bio);
        ArtistDto dto = new ArtistDto();
        dto.id = a.getId();
        dto.name = a.getName();
        dto.bio = a.getBio();
        return ResponseEntity.ok(dto);
    }

    // create album
    @PostMapping("/albums")
    public ResponseEntity<AlbumDto> createAlbum(@RequestParam @NotBlank String title,
                                                @RequestParam(required = false) String releaseDate,
                                                @RequestParam(required = false) String coverUrl,
                                                @RequestParam(required = false) Long artistId) {
        Album a = musicService.createAlbum(title, releaseDate, coverUrl, artistId);
        AlbumDto dto = new AlbumDto();
        dto.id = a.getId();
        dto.title = a.getTitle();
        dto.releaseDate = a.getReleaseDate() != null ? a.getReleaseDate().toString() : null;
        dto.coverUrl = a.getCoverUrl();
        dto.artistId = a.getArtist() != null ? a.getArtist().getId() : null;
        return ResponseEntity.ok(dto);
    }

    // upload song with file
    @PostMapping("/songs")
    public ResponseEntity<SongDto> uploadSong(@RequestParam @NotBlank String title,
                                              @RequestParam(required = false) Integer durationSeconds,
                                              @RequestParam(required = false) Long albumId,
                                              @RequestParam(required = false) Long artistId,
                                              @RequestPart("file") MultipartFile file) {
        Song s = musicService.uploadSong(title, durationSeconds, albumId, artistId, file);
        SongDto dto = new SongDto();
        dto.id = s.getId();
        dto.title = s.getTitle();
        dto.durationSeconds = s.getDurationSeconds();
        dto.mimeType = s.getMimeType();
        dto.filePath = s.getFilePath();
        dto.albumId = s.getAlbum() != null ? s.getAlbum().getId() : null;
        dto.artistId = s.getArtist() != null ? s.getArtist().getId() : null;
        return ResponseEntity.ok(dto);
    }
}
