package com.hoangpro.pikacover.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.adapter.SongListAdapter;
import com.hoangpro.pikacover.base.BaseFragment;
import com.hoangpro.pikacover.model.MyMessage;
import com.hoangpro.pikacover.model.SongFavorite;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.myinterface.DataResult;
import com.hoangpro.pikacover.sqlite.HandlerSaveSong;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_profile;
    }

    private RecyclerView rvSong;
    private SongListAdapter adapter;
    private List<SongJsonObject.Song> list;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        list = new ArrayList<>();
        adapter = new SongListAdapter(getActivity(), list);
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        HandlerSaveSong.GetListSongFavorite getListSongFavorite = new HandlerSaveSong.GetListSongFavorite(getActivity());
        getListSongFavorite.setDataResult(new DataResult() {
            @Override
            public void onDataResult(Object listResult) {
                List<SongFavorite> favorites = (List<SongFavorite>) listResult;
                for (SongFavorite songFavorite : favorites) {
                    list.add(new Gson().fromJson(songFavorite.getJson(), SongJsonObject.Song.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
        getListSongFavorite.execute();
    }

    private void initView(View view) {
        rvSong = view.findViewById(R.id.rvSong);
    }

    @Subscribe
    public void onEvent(final MyMessage myMessage) {
        final SongFavorite song = (SongFavorite) myMessage.getObject();
        switch (myMessage.getAction()) {
            case 0:
                final HandlerSaveSong.AddSongFavorite addSongFavorite = new HandlerSaveSong.AddSongFavorite(getActivity());
                addSongFavorite.setDataResult(new DataResult() {
                    @Override
                    public void onDataResult(Object listResult) {
                        list.add(new Gson().fromJson(song.getJson(), SongJsonObject.Song.class));
                        adapter.notifyItemRemoved(list.size()-1);
                        adapter.notifyItemRangeChanged(list.size()-1, list.size());
                    }
                });
                addSongFavorite.execute((SongFavorite) myMessage.getObject());
                break;
            case 1:
                HandlerSaveSong.DeleteSongFavorite deleteSongFavorite = new HandlerSaveSong.DeleteSongFavorite(getActivity());
                deleteSongFavorite.setDataResult(new DataResult() {
                    @Override
                    public void onDataResult(Object listResult) {
                        for(int i=0; i<list.size();i++){
                            if (list.get(i).getId()==song.getId()){
                                list.remove(i);
                                adapter.notifyItemRemoved(i);
                                adapter.notifyItemRangeChanged(i, list.size());
                                break;
                            }
                        }
                    }
                });
                deleteSongFavorite.execute(song);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
