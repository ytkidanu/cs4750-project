package com.cs4750.p5.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cs4750.p5.entity.Album;
import com.cs4750.p5.entity.Artist;
import com.cs4750.p5.entity.Song;
import com.cs4750.p5.repository.AlbumRepository;
import com.cs4750.p5.repository.ArtistRepository;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired 
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public ResponseEntity<Album> createAlbum(Album album, Integer user_id) {
        try {
            Optional<Artist> artistData = artistRepository.findById(user_id);
            if (artistData.isPresent()) {
                Artist associatedArtist = artistData.get();
                album.setArtist(associatedArtist);

                albumRepository.save(album);
                return new ResponseEntity<>(album, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FAILED_DEPENDENCY); // artist w/ input user_id doesn't exist in db
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Album>> getAllAlbums() {
        try {
            List<Album> albumList = albumRepository.findAll();
            if (albumList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<Album>>(albumList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Album> getAlbum(Integer albumId) {
        Optional<Album> albumData = albumRepository.findById(albumId);
        if (albumData.isPresent()) {
            return new ResponseEntity<>(albumData.get(), HttpStatus.OK);
        } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
    }

    public ResponseEntity<List<Song>> getAlbumSongs(Integer albumId) {
        try {
            Optional<Album> albumData = albumRepository.findById(albumId);
            if (albumData.isPresent()) {
                return new ResponseEntity<>(albumData.get().getAlbumSongs(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Album> updateAlbum(Integer albumId, Album newAlbum) {
        Optional<Album> albumData = albumRepository.findById(albumId);
        if (albumData.isPresent()) {
            Album oldAlbum = albumData.get();
            oldAlbum.setAlbumName(newAlbum.getAlbumName());
            oldAlbum.setNumOfSongs(newAlbum.getNumOfSongs());
            oldAlbum.setDuration(newAlbum.getDuration());
            oldAlbum.setReleaseDate(newAlbum.getReleaseDate());
            return new ResponseEntity<>(albumRepository.save(oldAlbum), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Album> deleteAlbum(Integer albumId) {
        try {
            Optional<Album> albumData = albumRepository.findById(albumId);
            if (albumData.isPresent()) {
                albumRepository.deleteById(albumId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
