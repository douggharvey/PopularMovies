package com.douglasharvey.popularmovies.ui;

import android.os.Bundle;
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
import com.douglasharvey.popularmovies.data.MovieAdapter;
import com.douglasharvey.popularmovies.utilities.FetchMoviesLoader;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;

import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    public static final int MOVIES_LOADER = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String MENU_SELECTION = "menu_selection";
    private MovieAdapter movieAdapter;
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
        loadMovieData(selectionType);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_SELECTION, selectionType);
    }

    private void loadMovieData(int selectionType) {
        if (NetworkUtils.isInternetAvailable(this)) {
            movieAdapter = new MovieAdapter(MainActivity.this);
            GridView gridView = findViewById(R.id.gv_movie_list);
            gridView.setAdapter(movieAdapter);
            callLoader(selectionType);
        } else {
            Toast.makeText(this, R.string.internet_connectivity_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callLoader(int selectionType) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(getString(R.string.MOVIES_SELECTION), selectionType);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (restartLoader) loaderManager.restartLoader(MOVIES_LOADER, queryBundle, this);
        else loaderManager.initLoader(MOVIES_LOADER, queryBundle, this);
        restartLoader = false;
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, final Bundle bundle) {
        return new FetchMoviesLoader(this, bundle);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieData) {
        movieAdapter.setMoviesData(movieData);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieAdapter.setMoviesData(null);
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
        if (selectionType == POPULAR) {
            menu.findItem(R.id.action_popular).setChecked(true);
        } else {
            menu.findItem(R.id.action_toprated).setChecked(true);
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

        return super.onOptionsItemSelected(item);
    }

}
