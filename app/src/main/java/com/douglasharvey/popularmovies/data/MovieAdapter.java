package com.douglasharvey.popularmovies.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context,0,movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movies, parent, false);
        }

        ImageView posterImageView = convertView.findViewById(R.id.grid_item_poster);

        //noinspection ConstantConditions
        Picasso.with(getContext())
                .load(NetworkUtils.buildPosterFullPath(movie.getPosterPath()))
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.unable_to_load_poster)
                .into(posterImageView);

        return convertView;
    }
}
