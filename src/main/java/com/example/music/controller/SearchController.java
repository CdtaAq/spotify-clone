package com.example.music.controller;

import com.example.music.model.Song;
import com.example.music.model.Artist;
import com.example.music.model.Album;
import com.example.music.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/songs")
    public List<Song> searchSongs(@RequestParam String keyword) {
        return searchService.searchSongs(keyword);
    }

    @GetMapping("/artists")
    public List<Artist> searchArtists(@RequestParam String keyword) {
        return searchService.searchArtists(keyword);
    }

    @GetMapping("/albums")
    public List<Album> searchAlbums(@RequestParam String keyword) {
        return searchService.searchAlbums(keyword);
    }

    @GetMapping("/trending")
    public List<Song> trendingSongs() {
        return searchService.trendingSongs();
    }

    @GetMapping("/obscure")
    public List<Song> obscureSongs() {
        return searchService.obscureSongs();
    }
}
