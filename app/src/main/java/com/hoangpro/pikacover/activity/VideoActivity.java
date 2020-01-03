package com.hoangpro.pikacover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.adapter.LyricAdapater;
import com.hoangpro.pikacover.api.GetLyricSongAPI;
import com.hoangpro.pikacover.model.LyricObject;
import com.hoangpro.pikacover.model.MyMessage;
import com.hoangpro.pikacover.model.SongFavorite;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.morefunct.MySession;
import com.hoangpro.pikacover.myinterface.DataResult;
import com.hoangpro.pikacover.sqlite.HandlerSaveSong;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener {

    private YouTubePlayerView ytPlay;
    private SongJsonObject.Song song;
    private YouTubePlayer player;
    private List<LyricObject.Datum> list;
    String URL = "http://pikasmart.com/api/appLyricFinderSongSentences/getListLyricsBylanguageCode?song_id=";
    private RecyclerView rvDatum;
    private LyricAdapater adapter;
    private Thread thread;
    private Handler handler;
    private final int UPDATE_CODE = 1001;
    private boolean isStop = false;
    private ImageView imgFavorite;
    private boolean isFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        song = new Gson().fromJson(MySession.currentJson, SongJsonObject.Song.class);
        Log.e("idSong", song.getId() + "");
        URL += song.getId() + "&language_code=vn";
        initView();
        setUpView();
        ytPlay.initialize(getString(R.string.youtube_api_key), this);
        list = new ArrayList<>();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (player != null) {
                    if (msg.what == UPDATE_CODE && !isStop) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }

    private void setUpView() {
        HandlerSaveSong.GetSongFavorite getSongFavorite = new HandlerSaveSong.GetSongFavorite(this);
        getSongFavorite.setDataResult(new DataResult() {
            @Override
            public void onDataResult(Object listResult) {
                SongFavorite SongFavo = (SongFavorite) listResult;
                if (SongFavo!=null) {
                    imgFavorite.setImageResource(R.drawable.ic_favorite_black);
                    isFavorite = true;
                } else {
                    imgFavorite.setImageResource(R.drawable.ic_favorite_border);
                    isFavorite = false;
                }
                Log.e("isFavorite", isFavorite+"");
            }
        });
        getSongFavorite.execute(song.getId());
    }

    private void initView() {
        ytPlay = findViewById(R.id.ytPlay);
        rvDatum = findViewById(R.id.rvDatum);
        imgFavorite = findViewById(R.id.imgFavorite);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer
            youTubePlayer, boolean b) {
        player = youTubePlayer;
        youTubePlayer.cueVideo(song.getUrl().split("v=")[1]);
        player.setPlayerStateChangeListener(this);
        adapter = new LyricAdapater(list, this, player, rvDatum);
        rvDatum.setAdapter(adapter);
        rvDatum.setLayoutManager(new LinearLayoutManager(this));
        GetLyricSongAPI getLyricSongAPI = new GetLyricSongAPI();
        getLyricSongAPI.setDataResult(new DataResult() {
            @Override
            public void onDataResult(Object result) {
                List<LyricObject.Datum> data = (List<LyricObject.Datum>) result;
                for (LyricObject.Datum datum : data) {
                    if (datum.getSentenceValue().length() > 0) {
                        datum.setFuriganaText();
                        list.add(datum);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        getLyricSongAPI.execute((URL + song.getId()).trim());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider
                                                provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 123);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            ytPlay.initialize(getString(R.string.youtube_api_key), this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {
        player.play();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Message message = new Message();
                    message.what = UPDATE_CODE;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null)
            if (!thread.isInterrupted())
                thread.interrupt();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onResume() {
        if (thread != null)
            if (thread.isInterrupted())
                thread.start();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if (thread != null)
            if (thread.isInterrupted()) {
                thread.start();
            }
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        if (thread != null)
            if (!thread.isInterrupted())
                thread.interrupt();
        isStop = true;
        player.pause();
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        if (thread != null)
            if (!thread.isInterrupted())
                thread.interrupt();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (thread != null)
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        super.onStop();
    }

    public void sentEventBus(View view) {
        SongFavorite songFavorite = new SongFavorite(song.getId(), new Gson().toJson(song));
        if (!isFavorite) {
            EventBus.getDefault().post(new MyMessage(0, songFavorite));
            imgFavorite.setImageResource(R.drawable.ic_favorite_black);
            isFavorite=true;
        } else {
            EventBus.getDefault().post(new MyMessage(1, songFavorite));
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
            isFavorite=false;
        }
    }
}
