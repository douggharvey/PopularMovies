package com.douglasharvey.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.data.Review;
import com.douglasharvey.popularmovies.data.Video;
import com.douglasharvey.popularmovies.utilities.DateUtils;
import com.douglasharvey.popularmovies.utilities.FetchReviewsLoader;
import com.douglasharvey.popularmovies.utilities.FetchVideosLoader;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final int VIDEOS_LOADER = 2;
    private static final int REVIEWS_LOADER = 3;

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
    @BindView(R.id.iv_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.rv_list_videos)
    RecyclerView rvListVideos;
    @BindView(R.id.rv_list_reviews)
    RecyclerView rvListReviews;

    private VideosAdapter videosAdapter;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setStatusBarTranslucent();
        ButterKnife.bind(this);
        Intent receivedIntent = getIntent();

        if (receivedIntent.hasExtra(getString(R.string.EXTRA_SELECTED_MOVIE))) {
            Bundle data = receivedIntent.getExtras();
            @SuppressWarnings("ConstantConditions") final Movie movie = data.getParcelable(getString(R.string.EXTRA_SELECTED_MOVIE));
            if (NetworkUtils.isInternetAvailable(this) && movie != null) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(getString(R.string.MOVIE_ID), movie.getId());
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(VIDEOS_LOADER, queryBundle, this);
                loaderManager.initLoader(REVIEWS_LOADER, queryBundle, this);
            } else {
                Toast.makeText(this, R.string.internet_connectivity_error, Toast.LENGTH_LONG).show();
            }
            populateUI(movie);
        }
    }

    private void setStatusBarTranslucent() {
        //https://stackoverflow.com/questions/29907615/android-transparent-status-bar-and-actionbar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void populateUI(final Movie movie) {
        setTextViews(movie);
        setBackdropPath(movie);
        setPosterPath(movie);
        setupVideoList();
        setupReviewList();
    }


    private void setTextViews(Movie movie) {
        tvMovieTitle.setText(movie.getTitle());
        tvSynopsisData.setText(movie.getOverview());
        tvReleaseDateData.setText(DateUtils.formatDate(movie.getReleaseDate()));
        tvVoteAverageData.setText(movie.getVoteAverage());
    }

    private void setBackdropPath(Movie movie) {
        Picasso.with(DetailActivity.this)
                .load(NetworkUtils.buildFullPath(movie.getBackdropPath()))
                //.placeholder(R.drawable.movie_placeholder) // decided not to have placeholder for this case
                .error(R.drawable.unable_to_load_poster)
                .into(ivBackdrop);
    }

    private void setPosterPath(Movie movie) {
        Picasso.with(DetailActivity.this)
                .load(NetworkUtils.buildFullPath(movie.getPosterPath()))
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.unable_to_load_poster)
                .into(ivPoster);
    }

    private void setupVideoList() {
        rvListVideos.addItemDecoration(new DividerItemDecoration(DetailActivity.this, LinearLayoutManager.HORIZONTAL));
        rvListVideos.setHasFixedSize(true);
        rvListVideos.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        videosAdapter = new VideosAdapter(DetailActivity.this);
        rvListVideos.setAdapter(videosAdapter);
    }

    private void setupReviewList() {
        rvListReviews.addItemDecoration(new DividerItemDecoration(DetailActivity.this, LinearLayoutManager.VERTICAL));
        rvListReviews.setHasFixedSize(true);
        rvListReviews.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));
        reviewsAdapter = new ReviewsAdapter(DetailActivity.this);
        rvListReviews.setAdapter(reviewsAdapter);
    }

    //Loaders

    @NonNull
    @Override
    public Loader onCreateLoader(int i, final Bundle bundle) {
        if (i == VIDEOS_LOADER) {
            return new FetchVideosLoader(this, bundle);
        } else {
            return new FetchReviewsLoader(this, bundle);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if (loader.getId() == VIDEOS_LOADER) {
            videosAdapter.setVideosData((List<Video>) data);
        } else {
            reviewsAdapter.setReviewsData((List<Review>) data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        if (loader.getId() == VIDEOS_LOADER) {
            videosAdapter.setVideosData(null);
        } else {
            reviewsAdapter.setReviewsData(null);
        }
    }
}
