package com.hoangpro.pikacover.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hoangpro.pikacover.fragment.FragmentMore;
import com.hoangpro.pikacover.fragment.FragmentNews;
import com.hoangpro.pikacover.fragment.FragmentProfile;
import com.hoangpro.pikacover.fragment.FragmentVideo;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new FragmentProfile();
            case 2: return new FragmentNews();
            case 3: return new FragmentMore();
            default: return new FragmentVideo();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
