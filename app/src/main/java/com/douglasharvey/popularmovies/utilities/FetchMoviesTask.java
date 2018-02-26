package com.douglasharvey.popularmovies.utilities;

import android.os.AsyncTask;

import com.douglasharvey.popularmovies.data.Movie;
import com.douglasharvey.popularmovies.data.MovieAdapter;

import java.net.URL;
import java.util.List;

public class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>> {

    private final MovieAdapter adapter;

    public FetchMoviesTask(MovieAdapter adapter) {
        this.adapter = adapter;
    }

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
        if (movieData != null) {
            adapter.setMoviesData(movieData);
        }
    }
}
