package com.hoangpro.pikacover.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.hoangpro.pikacover.model.LyricObject;
import com.hoangpro.pikacover.myinterface.DataResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetLyricSongAPI extends AsyncTask<String,Void, List<LyricObject.Datum>> {
    DataResult dataResult;

    public void setDataResult(DataResult dataResult) {
        this.dataResult = dataResult;
    }
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .writeTimeout(15,TimeUnit.SECONDS).build();
    @Override
    protected List<LyricObject.Datum> doInBackground(String... strings) {
        List<LyricObject.Datum> list=new ArrayList<>();
        Request.Builder builder = new Request.Builder();
        builder.url(strings[0]);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            LyricObject object = new Gson().fromJson(json, LyricObject.class);
            list.addAll(object.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<LyricObject.Datum> data) {
        dataResult.onDataResult(data);
        super.onPostExecute(data);
    }
}
