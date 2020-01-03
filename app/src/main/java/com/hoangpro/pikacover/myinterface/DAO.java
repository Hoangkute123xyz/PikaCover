package com.hoangpro.pikacover.myinterface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hoangpro.pikacover.model.SongFavorite;

import java.util.List;

@Dao
public interface DAO {
    @Query("select * from SongFavorite")
    List<SongFavorite> getAllSongFavorite();

    @Query("select * from SongFavorite where id = :id")
    SongFavorite getSongFavoriteById(int id);


    @Insert
    void addSongFavorite(SongFavorite...songFavorites);

    @Delete
    void deleteSongFavorite(SongFavorite...songFavorites);

    @Update
    void updateSongFavorite(SongFavorite...songFavorites);
}
