package com.hoangpro.pikacover.api;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.myinterface.DataResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetListSongAPI extends AsyncTask<String,Void, List<SongJsonObject.Song>> {
    private DataResult dataResult;
    private final int TIME_OUT = 15;
    public void setOndataResult(DataResult dataResult){
        this.dataResult=dataResult;
    }
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT,TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT,TimeUnit.SECONDS).build();
    @Override
    protected List<SongJsonObject.Song> doInBackground(String... strings) {
        List<SongJsonObject.Song> list= new ArrayList<>();
        Request.Builder builder = new Request.Builder();
        builder.url(strings[0]);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            String json=response.body().string();
            if (json.length()>0) {
                SongJsonObject object = new Gson().fromJson(json, SongJsonObject.class);
                list.addAll(object.getSong());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<SongJsonObject.Song> songs) {
        dataResult.onDataResult(songs);
        super.onPostExecute(songs);
    }
}
