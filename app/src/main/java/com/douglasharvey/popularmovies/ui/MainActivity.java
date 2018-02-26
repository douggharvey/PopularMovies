package com.douglasharvey.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.MovieAdapter;
import com.douglasharvey.popularmovies.utilities.FetchMoviesTask;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadMovieData(POPULAR);
    }

    private void loadMovieData(int selectionType) {
        if (NetworkUtils.isInternetAvailable(this)) {
            MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this);
            FetchMoviesTask moviesTask = new FetchMoviesTask(movieAdapter);
            GridView gridView = findViewById(R.id.gv_movie_list);
            gridView.setAdapter(movieAdapter);
            moviesTask.execute(selectionType);
        } else {
            Toast.makeText(this, R.string.internet_connectivity_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //toggle checked items so we know which is selected currently

        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        if (id == R.id.action_popular) {
            loadMovieData(POPULAR);
            return true;
        }
        if (id == R.id.action_toprated) {
            loadMovieData(TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
