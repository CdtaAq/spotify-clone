package com.example.music.model;

import jakarta.persistence.*;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user following someone
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // user being followed (nullable if artist is followed instead)
    @ManyToOne
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    // artist being followed (nullable if user is followed instead)
    @ManyToOne
    @JoinColumn(name = "following_artist_id")
    private Artist followingArtist;

    public Follow() {}

    public Follow(User follower, User followingUser, Artist followingArtist) {
        this.follower = follower;
        this.followingUser = followingUser;
        this.followingArtist = followingArtist;
    }

    public Long getId() { return id; }
    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }

    public User getFollowingUser() { return followingUser; }
    public void setFollowingUser(User followingUser) { this.followingUser = followingUser; }

    public Artist getFollowingArtist() { return followingArtist; }
    public void setFollowingArtist(Artist followingArtist) { this.followingArtist = followingArtist; }
}
