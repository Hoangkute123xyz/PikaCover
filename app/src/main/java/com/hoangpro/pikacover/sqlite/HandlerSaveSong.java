package com.hoangpro.pikacover.sqlite;

import android.content.Context;
import android.os.AsyncTask;

import com.hoangpro.pikacover.model.SongFavorite;
import com.hoangpro.pikacover.myinterface.DAO;
import com.hoangpro.pikacover.myinterface.DataResult;

import java.util.ArrayList;
import java.util.List;

public class HandlerSaveSong {

    public static class AddSongFavorite extends AsyncTask<SongFavorite,Void,Void>{
        private DAO dao;
        private DataResult dataResult;

        public void setDataResult(DataResult dataResult) {
            this.dataResult = dataResult;
        }

        public AddSongFavorite(Context context){
            dao=SongFavoriteSqlite.getInstance(context).getDao();
        }
        @Override
        protected Void doInBackground(SongFavorite... songFavorites) {
            dao.addSongFavorite(songFavorites);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dataResult.onDataResult(null);
            super.onPostExecute(aVoid);
        }
    }
    public static class GetListSongFavorite extends AsyncTask<Void,Void, List<SongFavorite>>{
        private  DataResult dataResult;
        private DAO dao;
        public GetListSongFavorite(Context context){
            dao=SongFavoriteSqlite.getInstance(context).getDao();
        }
        public void setDataResult(DataResult dataResult) {
            this.dataResult = dataResult;
        }

        @Override
        protected List<SongFavorite> doInBackground(Void... voids) {
            return dao.getAllSongFavorite();
        }

        @Override
        protected void onPostExecute(List<SongFavorite> songFavorites) {
            dataResult.onDataResult(songFavorites);
            super.onPostExecute(songFavorites);
        }
    }

    public static class DeleteSongFavorite extends AsyncTask<SongFavorite,Void,Void>{
        private DAO dao;
        private DataResult dataResult;

        public void setDataResult(DataResult dataResult) {
            this.dataResult = dataResult;
        }

        public DeleteSongFavorite(Context context){
            dao=SongFavoriteSqlite.getInstance(context).getDao();
        }
        @Override
        protected Void doInBackground(SongFavorite... songFavorites) {
            dao.deleteSongFavorite(songFavorites);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dataResult.onDataResult(null);
            super.onPostExecute(aVoid);
        }
    }
    public static class GetSongFavorite extends AsyncTask<Integer,Void,SongFavorite>{

        private DataResult dataResult;
        private DAO dao;
        public GetSongFavorite(Context context){
            dao=SongFavoriteSqlite.getInstance(context).getDao();
        }

        public void setDataResult(DataResult dataResult) {
            this.dataResult = dataResult;
        }

        @Override
        protected SongFavorite doInBackground(Integer... integers) {
            SongFavorite songFavorite = dao.getSongFavoriteById(integers[0]);
            return songFavorite;
        }

        @Override
        protected void onPostExecute(SongFavorite songFavorite) {
            dataResult.onDataResult(songFavorite);
            super.onPostExecute(songFavorite);
        }
    }
}
