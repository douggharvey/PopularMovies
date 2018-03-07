package com.douglasharvey.popularmovies.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.FavouritesContract;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.utilities.FetchMoviesLoader;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    private static final int FAVOURITES = 3;
    private static final int MOVIES_LOADER = 1;
    private static final int FAVOURITES_LOADER = 2;
    private static final String MENU_SELECTION = "menu_selection";
    private MoviesAdapter moviesAdapter;
    private boolean restartLoader = false;
    private int selectionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        selectionType = POPULAR;
        if (savedInstanceState != null) {
            selectionType = savedInstanceState.getInt(MENU_SELECTION);
        }
        if (!NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, R.string.error_internet_connectivity, Toast.LENGTH_SHORT).show();
        }
        loadMovieData(selectionType);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_SELECTION, selectionType);
    }

    private void loadMovieData(int selectionType) {
        moviesAdapter = new MoviesAdapter(MainActivity.this);
        GridView gridView = findViewById(R.id.gv_movie_list);
        gridView.setAdapter(moviesAdapter);
        callLoader(selectionType);
    }

    //Loader
    private void callLoader(int selectionType) {
        int loaderId;
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(getString(R.string.MOVIES_SELECTION), selectionType);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (selectionType == FAVOURITES) {
            loaderId = FAVOURITES_LOADER;
            loaderManager.destroyLoader(MOVIES_LOADER); // Prevent 2nd loader from running.
        } else {
            loaderId = MOVIES_LOADER;
            loaderManager.destroyLoader(FAVOURITES_LOADER); // Prevent 2nd loader from running.
        }
        //Loader<String> loader = loaderManager.getLoader(loaderId);
        if (restartLoader) {
            loaderManager.restartLoader(loaderId, queryBundle, this);
        } else {
            loaderManager.initLoader(loaderId, queryBundle, this);
        }

        restartLoader = false;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int loaderId, final Bundle bundle) {
        switch (loaderId) {
            case MOVIES_LOADER:
                return new FetchMoviesLoader(this, bundle);
            case FAVOURITES_LOADER:
                Uri favouritesQueryUri = FavouritesContract.FavouritesEntry.CONTENT_URI;
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
            case MOVIES_LOADER:
                //noinspection unchecked
                moviesAdapter.setMoviesData((List<Movie>) data);
                break;
            case FAVOURITES_LOADER:
                Cursor cursor = (Cursor) data;
                if (cursor.getCount() == 0)
                    Toast.makeText(this, R.string.no_favourites_message, Toast.LENGTH_SHORT).show();
                List<Movie> movieData = new ArrayList<>();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    movieData.add(new Movie(
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_RELEASE_DATE)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_POSTER)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_VOTE_AVERAGE)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_SYNOPSIS)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry._ID)),
                                    cursor.getString(cursor.getColumnIndex(FavouritesContract.FavouritesEntry.COLUMN_NAME_BACKDROP))
                            )
                    );
                    cursor.moveToNext();
                }
                moviesAdapter.setMoviesData(movieData);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        moviesAdapter.setMoviesData(null);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        switch (selectionType) {
            case POPULAR:
                menu.findItem(R.id.action_popular).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.action_toprated).setChecked(true);
                break;
            case FAVOURITES:
                menu.findItem(R.id.action_favourites).setChecked(true);
                break;
            default:
                Toast.makeText(this, getString(R.string.error_selection_type_not_implemented) + selectionType + " not implemented.", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //toggle checked items so we know which is selected currently

        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        if (id == R.id.action_popular) {
            restartLoader = true;
            selectionType = POPULAR;
            loadMovieData(selectionType);
            return true;
        }
        if (id == R.id.action_toprated) {
            restartLoader = true;
            selectionType = TOP_RATED;
            loadMovieData(selectionType);
            return true;
        }

        if (id == R.id.action_favourites) {
            restartLoader = true;
            selectionType = FAVOURITES;
            loadMovieData(selectionType);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
