package com.hoangpro.pikacover.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.adapter.SongListAdapter;
import com.hoangpro.pikacover.api.GetListSongAPI;
import com.hoangpro.pikacover.base.BaseFragment;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.morefunct.MyFunct;
import com.hoangpro.pikacover.morefunct.MySession;
import com.hoangpro.pikacover.myinterface.DataResult;

import java.util.ArrayList;
import java.util.List;

public class FragmentVideo extends BaseFragment implements DataResult {

    private String url = "http://pikasmart.com/api/Songs/listnew?limit=10&skip=";
    private int currentPage = 0;
    private GetListSongAPI getListSongAPI;
    private List<SongJsonObject.Song> list;
    private RecyclerView rvSong;
    private ProgressBar pgLoadMore;
    private SongListAdapter adapter;
    private boolean isLoading = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public int setLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView(view);
        list = new ArrayList<>();
        adapter = new SongListAdapter(getActivity(), list);
        rvSong.setAdapter(adapter);
        rvSong.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getListSongAPI = new GetListSongAPI();
        getListSongAPI.setOndataResult(this);
        getListSongAPI.execute(url + (currentPage * 10));

        rvSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() == list.size() - 1 && MyFunct.isNetWork(getActivity()) && isLoading) {
                    GetListSongAPI getListSongAPI = new GetListSongAPI();
                    getListSongAPI.setOndataResult(FragmentVideo.this);
                    getListSongAPI.execute(url + (currentPage * 10));
                    pgLoadMore.setVisibility(View.VISIBLE);
                    isLoading = false;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    private void intiView(View view) {
        rvSong = view.findViewById(R.id.rvSong);
        pgLoadMore = view.findViewById(R.id.pgLoadMore);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefresh);
    }

    @Override
    public void onDataResult(Object listResult) {
        List<SongJsonObject.Song> data = (List<SongJsonObject.Song>) listResult;
        list.addAll(data);
        adapter.notifyDataSetChanged();
        if (!MyFunct.isNetWork(getActivity()) || list.size()==0) {
            String json = MySession.getListSongFromCache(getActivity());
            if (json.length() > 0) {
                List<SongJsonObject.Song> songList = new Gson().fromJson(json, SongJsonObject.class).getSong();
                if (songList != null)
                    list.addAll(songList);
            }
        } else if (currentPage == 0 && list.size() > 0) {
            SongJsonObject object = new SongJsonObject();
            object.setSong(list);
            MySession.saveListSong(getActivity(), new Gson().toJson(object));
        }
        if (data.size()>0) {
            currentPage++;
            isLoading = true;
        } else {
            isLoading=false;
        }
        pgLoadMore.setVisibility(View.GONE);
    }
}
