package com.douglasharvey.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.FavouritesContract;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.data.Review;
import com.douglasharvey.popularmovies.data.Video;
import com.douglasharvey.popularmovies.utilities.DateUtils;
import com.douglasharvey.popularmovies.utilities.FetchReviewsLoader;
import com.douglasharvey.popularmovies.utilities.FetchVideosLoader;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final int VIDEOS_LOADER = 3;
    private static final int REVIEWS_LOADER = 4;
    private static final int FAVOURITES_LOADER = 6;
    private static Movie movie;

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
    @BindView(R.id.action_favourites)
    MaterialFavoriteButton actionFavourites;

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
            //noinspection ConstantConditions
            movie = data.getParcelable(getString(R.string.EXTRA_SELECTED_MOVIE));
            Bundle queryBundle = new Bundle();
            queryBundle.putString(getString(R.string.MOVIE_ID), movie.getId());
            if (movie != null) {
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(FAVOURITES_LOADER, queryBundle, this);
                loaderManager.initLoader(VIDEOS_LOADER, queryBundle, this);
                loaderManager.initLoader(REVIEWS_LOADER, queryBundle, this);
            }
            populateUI();
        }
    }


    private void storeFavourite() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavouritesContract.FavouritesEntry._ID, movie.getId());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE, movie.getTitle());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_BACKDROP, movie.getBackdropPath());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_POSTER, movie.getPosterPath());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_SYNOPSIS, movie.getOverview());
        contentValues.put(FavouritesContract.FavouritesEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());

        @SuppressLint("HandlerLeak") AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                Toast.makeText(DetailActivity.this, R.string.add_to_favourites_message, Toast.LENGTH_SHORT).show();
            }
        };

        asyncQueryHandler.startInsert(1, null, FavouritesContract.FavouritesEntry.CONTENT_URI, contentValues);

    }

    private void removeFavourites() {
        Uri uri = FavouritesContract.FavouritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movie.getId()).build();

        @SuppressLint("HandlerLeak") AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int rowsDeleted) {
                super.onDeleteComplete(token, cookie, rowsDeleted);
                if (rowsDeleted == 1) {
                    Toast.makeText(DetailActivity.this, R.string.removed_from_favourites_message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        asyncQueryHandler.startDelete(2,null,uri, null, null);
    }

    private void setStatusBarTranslucent() {
        //https://stackoverflow.com/questions/29907615/android-transparent-status-bar-and-actionbar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void populateUI() {
        actionFavourites.setVisibility(View.INVISIBLE);
        actionFavourites.setAnimateFavorite(false);
        setTextViews();
        setBackdropPath();
        setPosterPath();
        setupVideoList();
        setupReviewList();
    }


    private void setTextViews() {
        tvMovieTitle.setText(movie.getTitle());
        tvSynopsisData.setText(movie.getOverview());
        tvReleaseDateData.setText(DateUtils.formatDate(movie.getReleaseDate()));
        tvVoteAverageData.setText(movie.getVoteAverage());
    }

    private void setBackdropPath() {
        Picasso.with(DetailActivity.this)
                .load(NetworkUtils.buildFullPath(movie.getBackdropPath()))
                //.placeholder(R.drawable.movie_placeholder) // decided not to have placeholder for this case
                .error(R.drawable.unable_to_load_poster)
                .into(ivBackdrop);
    }

    private void setPosterPath() {
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
    public Loader onCreateLoader(int loaderId, final Bundle bundle) {
        switch (loaderId) {
            case VIDEOS_LOADER:
                return new FetchVideosLoader(this, bundle);
            case REVIEWS_LOADER:
                return new FetchReviewsLoader(this, bundle);
            case FAVOURITES_LOADER:
                Uri favouritesQueryUri = FavouritesContract.FavouritesEntry.CONTENT_URI;
                favouritesQueryUri = favouritesQueryUri.buildUpon().appendPath(movie.getId()).build();
                return new CursorLoader(this,
                        favouritesQueryUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        switch (loader.getId()) {
            case VIDEOS_LOADER:
                //noinspection unchecked
                videosAdapter.setVideosData((List<Video>) data);
                break;
            case REVIEWS_LOADER:
                //noinspection unchecked
                reviewsAdapter.setReviewsData((List<Review>) data);
                break;
            case FAVOURITES_LOADER:
                Cursor cursor = (Cursor) data;
                if (cursor.getCount() == 1) actionFavourites.setFavorite(true);
                else actionFavourites.setFavorite(false);
                actionFavourites.setVisibility(View.VISIBLE);
                actionFavourites.setAnimateFavorite(true);
                setOnFavouriteChangeListener();
                break;
        }
    }

    private void setOnFavouriteChangeListener() {
        actionFavourites.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        //only 1 row so ok to execute db transaction on main thread
                        if (favorite)
                            storeFavourite();
                        else
                            removeFavourites();
                    }
                });
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        switch (loader.getId()) {
            case VIDEOS_LOADER:
                videosAdapter.setVideosData(null);
                break;
            case REVIEWS_LOADER:
                reviewsAdapter.setReviewsData(null);
                break;
            case FAVOURITES_LOADER:
                //do nothing
                break;
        }
    }
}
