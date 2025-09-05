package com.example.music.service;

import com.example.music.model.Song;
import com.example.music.model.Artist;
import com.example.music.model.Album;
import com.example.music.repository.SongRepository;
import com.example.music.repository.ArtistRepository;
import com.example.music.repository.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public SearchService(SongRepository songRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public List<Song> searchSongs(String keyword) {
        return songRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Artist> searchArtists(String keyword) {
        return artistRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Album> searchAlbums(String keyword) {
        return albumRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Song> trendingSongs() {
        return songRepository.findTop10ByOrderByPlayCountDesc();
    }

    public List<Song> obscureSongs() {
        return songRepository.findTop10ByOrderByPlayCountAsc();
    }
}
