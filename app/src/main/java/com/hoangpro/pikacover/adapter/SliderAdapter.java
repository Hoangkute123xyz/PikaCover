package com.hoangpro.pikacover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.model.SongJsonObject;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<SongJsonObject.Song> list;

    public SliderAdapter(Context context, List<SongJsonObject.Song> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SongJsonObject.Song song = list.get(position);
        View view=LayoutInflater.from(context).inflate(R.layout.item_slider, container,false);
        TextView tvSlider = view.findViewById(R.id.tvSlider);
        ImageView imgSlider = view.findViewById(R.id.imgSlider);
        Glide.with(context).load(song.getThumbnail()).into(imgSlider);
        tvSlider.setText(song.getName()+"\n"+song.getNameVn());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object==view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
