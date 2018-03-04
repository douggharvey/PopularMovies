package com.douglasharvey.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.utilities.FetchMoviesLoader;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;

import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    public static final int FAVOURITES = 3;
    private static final int MOVIES_LOADER = 1;
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
        loadMovieData(selectionType); //to do check if this is favourites


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_SELECTION, selectionType);
    }

    private void loadMovieData(int selectionType) {
        if (NetworkUtils.isInternetAvailable(this)) {
            moviesAdapter = new MoviesAdapter(MainActivity.this);
            GridView gridView = findViewById(R.id.gv_movie_list);
            gridView.setAdapter(moviesAdapter);
            callLoader(selectionType);
        } else {
            Toast.makeText(this, R.string.internet_connectivity_error, Toast.LENGTH_LONG).show();
        }
    }

    //Loader
    private void callLoader(int selectionType) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(getString(R.string.MOVIES_SELECTION), selectionType);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (restartLoader) loaderManager.restartLoader(MOVIES_LOADER, queryBundle, this);
        else loaderManager.initLoader(MOVIES_LOADER, queryBundle, this);
        restartLoader = false;
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, final Bundle bundle) {
        return new FetchMoviesLoader(this, bundle);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movieData) {
        moviesAdapter.setMoviesData(movieData);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        moviesAdapter.setMoviesData(null);
    }


    // Favourites
    private void loadFavourites() {
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
                Toast.makeText(this, "ERROR: OnPrepareOptionsMenu + selectionType: "+ selectionType + " not implemented.", Toast.LENGTH_LONG).show();
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
            selectionType = FAVOURITES;
            loadFavourites();
            return true;
        }   

        return super.onOptionsItemSelected(item);
    }

}
