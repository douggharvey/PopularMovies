package com.douglasharvey.popularmovies.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Review;

import java.net.URL;
import java.util.List;

public class FetchReviewsLoader extends AsyncTaskLoader<List<Review>> {

    private final Bundle args;
    private List<Review> reviewList;
    private static final int REVIEWS = 3;
    private static final String LOG_TAG = FetchReviewsLoader.class.getSimpleName();

    public FetchReviewsLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        if (reviewList == null) forceLoad();
        else deliverResult(reviewList);
    }

    @Override
    public List<Review> loadInBackground() {

        String movieId = args.getString(getContext().getString(R.string.MOVIE_ID));

        URL movieRequestUrl = NetworkUtils.buildUrl(REVIEWS, movieId);

        try {
            String jsonReviewResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            Log.d(LOG_TAG, "loadInBackground: response " + jsonReviewResponse);
            return JsonReviewUtils.getReviewStringsFromJson(jsonReviewResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(final List<Review> reviewData) {
        reviewList = reviewData;
        super.deliverResult(reviewData);
    }

}
