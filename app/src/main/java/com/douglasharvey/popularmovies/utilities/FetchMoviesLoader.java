package com.douglasharvey.popularmovies.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Movie;

import java.net.URL;
import java.util.List;

public class FetchMoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private final Bundle args;
    private List<Movie> movieList;

    public FetchMoviesLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        if (movieList == null) forceLoad();
        else deliverResult(movieList);
    }

    @Override
    public List<Movie> loadInBackground() {

        int queryType = args.getInt(getContext().getString(R.string.MOVIES_SELECTION));

        URL movieRequestUrl = NetworkUtils.buildUrl(queryType, null);

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            return JsonMovieUtils.getMovieStringsFromJson(jsonMovieResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(final List<Movie> movieData) {
        movieList = movieData;
        super.deliverResult(movieData);
    }

}
