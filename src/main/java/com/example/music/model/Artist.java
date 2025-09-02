package com.example.music.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 2000)
    private String biography;

    // Popularity score (0 = obscure, higher = popular)
    private int popularityScore;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums = new ArrayList<>();

    // === Constructors ===
    public Artist() {}

    public Artist(String name, String biography, int popularityScore) {
        this.name = name;
        this.biography = biography;
        this.popularityScore = popularityScore;
    }

    // === Getters and Setters ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore = popularityScore;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    // Helper method to add album
    public void addAlbum(Album album) {
        albums.add(album);
        album.setArtist(this);
    }

    // Helper method to remove album
    public void removeAlbum(Album album) {
        albums.remove(album);
        album.setArtist(null);
    }
}
