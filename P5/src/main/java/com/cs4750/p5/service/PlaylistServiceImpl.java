package com.cs4750.p5.service;

import java.util.Optional;
import java.util.List;

import com.cs4750.p5.entity.Playlist;
import com.cs4750.p5.entity.Song;
import com.cs4750.p5.entity.User;
import com.cs4750.p5.repository.PlaylistRepository;
import com.cs4750.p5.repository.SongRepository;
import com.cs4750.p5.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlaylistRepository playlistRepo;
    
    @Autowired
    private SongRepository songRepo; 

    public ResponseEntity<Playlist> createPlaylist(Playlist playlist, Integer userId) {
        try {
            Optional<User> userData = userRepo.findById(userId);
            if (userData.isPresent()) {
                User associatedUser = userData.get();
                playlist.setUser(associatedUser);

                playlistRepo.save(playlist);
                return new ResponseEntity<>(playlist, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY); // user w/ input userId doesn't exist in db
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        try {
            List<Playlist> playlistList = playlistRepo.findAll();
            if (playlistList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<Playlist>>(playlistList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Playlist> getPlaylist(Integer id) {
        Optional<Playlist> playlistData = playlistRepo.findById(id); // findById() is built-in for JPA

        if (playlistData.isPresent()) {
            return new ResponseEntity<>(playlistData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Song>> getPlaylistSongs(Integer id) {
        try {
            Optional<Playlist> playlistData = playlistRepo.findById(id);
            if (playlistData.isPresent()) {
                return new ResponseEntity<>(playlistData.get().getPlaylistSongs(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Playlist> updatePlaylist(Integer id, Playlist playlist) {
        Optional<Playlist> playlistData = playlistRepo.findById(id);

        if (playlistData.isPresent()) {
            Playlist _playlist = playlistData.get();
            _playlist.setPlaylistName(playlist.getPlaylistName());
            _playlist.setNumOfSongs(playlist.getNumOfSongs());
            _playlist.setDuration(playlist.getDuration());
            _playlist.setDescription(playlist.getDescription());
            _playlist.setStatus(playlist.getStatus());
            return new ResponseEntity<>(playlistRepo.save(_playlist), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Playlist> deletePlaylist(Integer id) {
        try {
            Optional<Playlist> playlistData = playlistRepo.findById(id);
            if (playlistData.isPresent()) {
                playlistRepo.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Playlist> addSongToPlaylist(Integer playlistId, Integer songId) {
        try {
            Optional<Playlist> playlistData = playlistRepo.findById(playlistId);
            Optional<Song> songData = songRepo.findById(songId);
            if (playlistData.isPresent()) {
                if (songData.isPresent()) {
                    Playlist curPlaylist = playlistData.get();
                    Song addedSong = songData.get();
                    curPlaylist.getPlaylistSongs().add(addedSong);
                    return new ResponseEntity<>(playlistRepo.save(curPlaylist), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // song not in db
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // playlist not in db
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Playlist> deleteSongFromPlaylist(Integer playlistId, Integer songId) {
        try {
            Optional<Playlist> playlistData = playlistRepo.findById(playlistId);
            Optional<Song> songData = songRepo.findById(songId);
            if (playlistData.isPresent()) {
                Playlist curPlaylist = playlistData.get();
                Song removedSong = songData.get();
                if (curPlaylist.getPlaylistSongs().contains(removedSong)) {
                    curPlaylist.getPlaylistSongs().remove(removedSong);
                    return new ResponseEntity<>(playlistRepo.save(curPlaylist), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // song not in playlist
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // playlist not in db
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}