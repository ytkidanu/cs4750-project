package com.cs4750.p5.repository;


import com.cs4750.p5.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

}