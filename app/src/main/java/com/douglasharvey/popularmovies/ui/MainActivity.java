package com.douglasharvey.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.data.MovieAdapter;
import com.douglasharvey.popularmovies.utilities.JsonUtils;
import com.douglasharvey.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

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
        if (NetworkUtils.isInternetAvailable(this))
            new FetchMoviesTask().execute(selectionType);
        else {
            Toast.makeText(this, R.string.internet_connectivity_error, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }

            int queryType = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(queryType);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                return JsonUtils.getMovieStringsFromJson(jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Movie> movieData) {

            MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, movieData);
            GridView gridView = findViewById(R.id.gv_movie_list);
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                    Movie selectedMovie = movieData.get(position);
                    //Learnt how to parcel object from this link:
                    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
                    detailIntent.putExtra(getString(R.string.EXTRA_SELECTED_MOVIE), selectedMovie);
                    startActivity(detailIntent);
                }
            });
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
