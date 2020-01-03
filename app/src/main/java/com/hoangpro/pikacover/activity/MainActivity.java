package com.hoangpro.pikacover.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.adapter.MainViewPagerAdapter;
import com.hoangpro.pikacover.api.GetListSongAPI;
import com.hoangpro.pikacover.customview.MyViewPager;
import com.hoangpro.pikacover.model.MyMessage;
import com.hoangpro.pikacover.model.SongFavorite;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.morefunct.MySession;
import com.hoangpro.pikacover.myinterface.DataResult;
import com.hoangpro.pikacover.sqlite.SongFavoriteSqlite;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private MyViewPager viewPager;
    private BottomNavigationView bottomNav;
    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        adapter=new MainViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(adapter);
        viewPager.setSwipePaging(false);
        viewPager.setOffscreenPageLimit(4);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNav);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item_home: viewPager.setCurrentItem(0,false); break;
            case R.id.item_profile: viewPager.setCurrentItem(1, false); break;
            case R.id.item_news: viewPager.setCurrentItem(2,false); break;
            case R.id.item_more: viewPager.setCurrentItem(3,false); break;
        }
        return true;
    }
}
