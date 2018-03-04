package com.douglasharvey.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private List<Video> videoList = new ArrayList<>();
    private final Context context;


    void setVideosData(List<Video> list) {
        if (list != null) {
            videoList = list;
            notifyDataSetChanged();
        }
    }

    VideosAdapter(@NonNull Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_videos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.video = videoList.get(position);
        holder.tvVideoName.setText(holder.video.getName());

        setImageForVideo(holder);

        holder.ivTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo(context, holder.video.getKey());
            }

        });
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Video video;
        @BindView(R.id.iv_trailer)
        ImageView ivTrailer;
        @BindView(R.id.tv_video_name)
        TextView tvVideoName;

        public ViewHolder(View view) {
            super(view);
            ivTrailer = view.findViewById(R.id.iv_trailer);
            tvVideoName = view.findViewById(R.id.tv_video_name);
        }
    }

    private void setImageForVideo(@NonNull ViewHolder holder) {
        Picasso.with(context)
                .load("https://img.youtube.com/vi/" + holder.video.getKey() + "/0.jpg")
                //.placeholder(R.drawable.movie_placeholder) // decided not to use placeholder for this case
                .error(R.drawable.unable_to_load_poster)
                .into(holder.ivTrailer);
    }

    // from https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
    // https://stackoverflow.com/questions/15407502/how-to-check-if-an-intent-can-be-handled-from-some-activity
    // https://stackoverflow.com/questions/45144574/how-to-play-any-youtube-video-play-in-full-screen-in-android-app
    private static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        PackageManager packageManager = context.getPackageManager();
        if (appIntent.resolveActivity(packageManager) != null) {
            appIntent.putExtra("force_fullscreen", true);
            context.startActivity(appIntent);
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            if (webIntent.resolveActivity(packageManager) != null) context.startActivity(webIntent);
            else
                Toast.makeText(context, R.string.error_loading_youtube, Toast.LENGTH_SHORT).show();
        }
    }

}
