package com.hoangpro.pikacover.sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hoangpro.pikacover.model.SongFavorite;
import com.hoangpro.pikacover.myinterface.DAO;

@Database(version = 1, entities = {SongFavorite.class})
public abstract class SongFavoriteSqlite extends RoomDatabase {
    public abstract DAO getDao();
    private static SongFavoriteSqlite instance;
    public static SongFavoriteSqlite getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context, SongFavoriteSqlite.class, "Song.dat")
                    .enableMultiInstanceInvalidation().build();
        }
        return instance;
    }

    public void DestroyInstance(){
        if (instance!=null){
            instance=null;
        }
    }
}
