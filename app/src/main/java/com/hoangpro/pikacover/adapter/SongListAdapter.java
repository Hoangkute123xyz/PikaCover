package com.hoangpro.pikacover.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hoangpro.pikacover.R;
import com.hoangpro.pikacover.activity.VideoActivity;
import com.hoangpro.pikacover.model.SongJsonObject;
import com.hoangpro.pikacover.morefunct.MySession;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongHolder> {
    class SongHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView tvView;
        TextView tvDuration;
        TextView tvNameJ;
        TextView tvNameVn;
        TextView tvLevel;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            tvView = itemView.findViewById(R.id.tvView);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvNameJ = itemView.findViewById(R.id.tvNameJ);
            tvNameVn = itemView.findViewById(R.id.tvNameVn);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }
    }
    private Context context;
    private List<SongJsonObject.Song> list;

    public SongListAdapter(Context context, List<SongJsonObject.Song> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongHolder(LayoutInflater.from(context).inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        final SongJsonObject.Song post = list.get(position);
        Glide.with(context).load(post.getThumbnail()).into(holder.imgThumb);
        holder.tvView.setText(post.getView() + "");
        if (post.getView()>=1000){
            holder.tvView.setText(String.format("%.2fk", (double)post.getView()/1000));
        }
        holder.tvDuration.setText(post.getVideoLength());
        holder.tvNameJ.setText(post.getName());
        holder.tvNameVn.setText(post.getNameVn());
        holder.tvLevel.setText(getLevel(post.getLevelId(),holder));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySession.currentJson= new Gson().toJson(post);
                Intent intent =new Intent(context,VideoActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private String getLevel(int Level,SongHolder holder) {
        switch (Level) {
            case 12:
                holder.tvLevel.setBackgroundResource(R.drawable.label_medium);
                return context.getString(R.string.medium);
            case 13:
                holder.tvLevel.setBackgroundResource(R.drawable.label_hard);
                return context.getString(R.string.hard);
            default:
                holder.tvLevel.setBackgroundResource(R.drawable.label_easy);
                return context.getString(R.string.easy);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}