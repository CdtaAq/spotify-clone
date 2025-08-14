package com.example.music.controller;

import com.example.music.dto.AlbumDto;
import com.example.music.dto.ArtistDto;
import com.example.music.dto.SongDto;
import com.example.music.model.Album;
import com.example.music.model.Artist;
import com.example.music.model.Song;
import com.example.music.service.MusicService;
import com.example.music.service.StorageService;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PublicController {

    private final MusicService musicService;
    private final StorageService storageService;

    public PublicController(MusicService musicService, StorageService storageService) {
        this.musicService = musicService;
        this.storageService = storageService;
    }

    @GetMapping("/artists")
    public List<ArtistDto> listArtists() {
        return musicService.listArtists().stream().map(a -> {
            ArtistDto dto = new ArtistDto();
            dto.id = a.getId();
            dto.name = a.getName();
            dto.bio = a.getBio();
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/albums")
    public List<AlbumDto> listAlbums(@RequestParam(required = false) Long artistId) {
        List<Album> albums = (artistId == null) ? musicService.listAlbums() : musicService.listAlbumsByArtist(artistId);
        return albums.stream().map(a -> {
            AlbumDto dto = new AlbumDto();
            dto.id = a.getId();
            dto.title = a.getTitle();
            dto.releaseDate = a.getReleaseDate() != null ? a.getReleaseDate().toString() : null;
            dto.coverUrl = a.getCoverUrl();
            dto.artistId = a.getArtist() != null ? a.getArtist().getId() : null;
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/songs")
    public List<SongDto> listSongs(@RequestParam(required = false) Long artistId,
                                   @RequestParam(required = false) Long albumId) {
        List<Song> songs;
        if (artistId != null) songs = musicService.listSongsByArtist(artistId);
        else if (albumId != null) songs = musicService.listSongsByAlbum(albumId);
        else songs = musicService.listSongs();

        return songs.stream().map(s -> {
            SongDto dto = new SongDto();
            dto.id = s.getId();
            dto.title = s.getTitle();
            dto.durationSeconds = s.getDurationSeconds();
            dto.mimeType = s.getMimeType();
            dto.filePath = s.getFilePath();
            dto.albumId = s.getAlbum() != null ? s.getAlbum().getId() : null;
            dto.artistId = s.getArtist() != null ? s.getArtist().getId() : null;
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        Song s = musicService.findSong(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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

    /**
     * Download raw file bytes (useful for local dev - in production you'd serve via CDN or signed URL).
     * Example: GET /api/songs/stream/1
     */
    @GetMapping("/songs/stream/{id}")
    public ResponseEntity<Resource> streamSong(@PathVariable Long id,
                                               @RequestHeader(value = "Range", required = false) String rangeHeader) {
        Song s = musicService.findSong(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String rel = s.getFilePath();
        if (!storageService.exists(rel)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found on disk");
        }
        Path path = storageService.resolvePath(rel);
        Resource resource = new FileSystemResource(path.toFile());

        // If Range header present, we should support partial content. For brevity we stream whole file.
        // In production implement proper Range support for seeking.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(s.getMimeType() != null ? MediaType.parseMediaType(s.getMimeType()) : MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(path.toFile().length());
        headers.setContentDisposition(ContentDisposition.attachment().filename(path.getFileName().toString()).build());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
