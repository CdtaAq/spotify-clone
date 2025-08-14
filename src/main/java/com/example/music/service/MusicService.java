package com.example.music.service;

import com.example.music.model.Album;
import com.example.music.model.Artist;
import com.example.music.model.Song;
import com.example.music.repo.AlbumRepository;
import com.example.music.repo.ArtistRepository;
import com.example.music.repo.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class MusicService {

    private final ArtistRepository artistRepo;
    private final AlbumRepository albumRepo;
    private final SongRepository songRepo;
    private final StorageService storage;

    public MusicService(ArtistRepository artistRepo,
                        AlbumRepository albumRepo,
                        SongRepository songRepo,
                        StorageService storage) {
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
        this.songRepo = songRepo;
        this.storage = storage;
    }

    // Artist
    public Artist createArtist(String name, String bio) {
        Artist a = new Artist();
        a.setName(name);
        a.setBio(bio);
        return artistRepo.save(a);
    }

    public Optional<Artist> findArtist(Long id) { return artistRepo.findById(id); }
    public List<Artist> listArtists() { return artistRepo.findAll(); }

    // Album
    public Album createAlbum(String title, String releaseDateStr, String coverUrl, Long artistId) {
        Album alb = new Album();
        alb.setTitle(title);
        if (releaseDateStr != null && !releaseDateStr.isBlank()) {
            alb.setReleaseDate(java.time.LocalDate.parse(releaseDateStr));
        }
        alb.setCoverUrl(coverUrl);
        if (artistId != null) {
            Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Artist not found"));
            alb.setArtist(artist);
        }
        return albumRepo.save(alb);
    }

    public Optional<Album> findAlbum(Long id) { return albumRepo.findById(id); }
    public List<Album> listAlbums() { return albumRepo.findAll(); }
    public List<Album> listAlbumsByArtist(Long artistId) { return albumRepo.findByArtistId(artistId); }

    // Song upload + metadata
    public Song uploadSong(String title, Integer durationSeconds, Long albumId, Long artistId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Missing file");
        }
        // store file
        String saved = storage.saveFile(file);

        Song s = new Song();
        s.setTitle(title);
        s.setDurationSeconds(durationSeconds);
        s.setMimeType(file.getContentType());
        s.setFilePath(saved);

        if (albumId != null) {
            Album album = albumRepo.findById(albumId).orElseThrow(() -> new IllegalArgumentException("Album not found"));
            s.setAlbum(album);
        }

        if (artistId != null) {
            Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Artist not found"));
            s.setArtist(artist);
        }
        return songRepo.save(s);
    }

    public Optional<Song> findSong(Long id) { return songRepo.findById(id); }
    public List<Song> listSongs() { return songRepo.findAll(); }
    public List<Song> listSongsByArtist(Long artistId) { return songRepo.findByArtistId(artistId); }
    public List<Song> listSongsByAlbum(Long albumId) { return songRepo.findByAlbumId(albumId); }
}
