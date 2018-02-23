package com.douglasharvey.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.utilities.DateUtils;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_synopsis_data)
    TextView tvSynopsisData;
    @BindView(R.id.tv_release_date_data)
    TextView tvReleaseDateData;
    @BindView(R.id.tv_vote_average_data)
    TextView tvVoteAverageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent receivedIntent = getIntent();

        if (receivedIntent.hasExtra(getString(R.string.EXTRA_SELECTED_MOVIE))) {
            Bundle data = receivedIntent.getExtras();
            @SuppressWarnings("ConstantConditions") final Movie movie = data.getParcelable(getString(R.string.EXTRA_SELECTED_MOVIE));
            //noinspection ConstantConditions
            tvMovieTitle.setText(movie.getTitle());
            tvSynopsisData.setText(movie.getOverview());
            tvReleaseDateData.setText(DateUtils.formatDate(movie.getReleaseDate()));
            tvVoteAverageData.setText(movie.getVoteAverage());
            Picasso.with(DetailActivity.this)
                    .load(NetworkUtils.buildPosterFullPath(movie.getPosterPath()))
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.unable_to_load_poster)
                    .into(ivPoster);
        }
    }
}
