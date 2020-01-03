package com.hoangpro.pikacover.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class SongFavorite {
    @PrimaryKey()
    private int id;
    @ColumnInfo
    private String json;

    public SongFavorite(int id, String json) {
        this.id = id;
        this.json = json;
    }

    public int getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
