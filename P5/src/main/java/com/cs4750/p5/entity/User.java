package com.cs4750.p5.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="[user]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="plan_id")
    private Integer planId;

    @Column(name="username")
    private String username;

    @Column(name="encrypted_password")
    private byte[] password;

    @Column(name="email")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name="date_joined")
    private LocalDate dateJoined;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // referenced by Artist entity
    @JsonBackReference
    Artist artist;

    @OneToMany(mappedBy = "owningUser", cascade = CascadeType.ALL) // referenced by Playlist entity
    @JsonIgnore
    private List<Playlist> playlists = new ArrayList<>();

    // add fkey mapping for planid if implemented (hard-coded default plan_id = 1 for now)

    User() {}

    User(Integer userId, Integer planId, String username, byte[] password, String email, LocalDate dateJoined) {
        this.userId = userId;
        this.planId = planId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateJoined = dateJoined;
    }

    public User(Integer planId, String username, byte[] password, String email, LocalDate dateJoined) {
        this.planId = planId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateJoined = dateJoined;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
