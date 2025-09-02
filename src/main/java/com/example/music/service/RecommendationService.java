package com.example.music.service;

import com.example.music.model.Album;
import com.example.music.model.Artist;
import com.example.music.model.Song;
import com.example.music.repository.AlbumRepository;
import com.example.music.repository.ArtistRepository;
import com.example.music.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public RecommendationService(ArtistRepository artistRepository,
                                 SongRepository songRepository,
                                 AlbumRepository albumRepository) {
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    // 🔹 Recommend obscure artists
    public List<Artist> recommendObscureArtists(String currentArtistName) {
        Artist current = artistRepository.findByName(currentArtistName);
        if (current == null) throw new RuntimeException("Artist not found: " + currentArtistName);

        return artistRepository.findByPopularityScoreLessThan(current.getPopularityScore())
                .stream()
                .sorted(Comparator.comparingInt(Artist::getPopularityScore))
                .limit(5)
                .collect(Collectors.toList());
    }

    // 🔹 Recommend obscure songs
    public List<Song> recommendObscureSongs(String currentSongTitle) {
        Song current = songRepository.findByTitle(currentSongTitle);
        if (current == null) throw new RuntimeException("Song not found: " + currentSongTitle);

        return songRepository.findByPopularityScoreLessThan(current.getPopularityScore())
                .stream()
                .sorted(Comparator.comparingInt(Song::getPopularityScore))
                .limit(5)
                .collect(Collectors.toList());
    }

    // 🔹 Recommend obscure albums
    public List<Album> recommendObscureAlbums(String currentAlbumTitle) {
        Album current = albumRepository.findByTitle(currentAlbumTitle);
        if (current == null) throw new RuntimeException("Album not found: " + currentAlbumTitle);

        return albumRepository.findByPopularityScoreLessThan(current.getPopularityScore())
                .stream()
                .sorted(Comparator.comparingInt(Album::getPopularityScore))
                .limit(5)
                .collect(Collectors.toList());
    }
}
