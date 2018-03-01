package com.douglasharvey.popularmovies.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Video;

import java.net.URL;
import java.util.List;

public class FetchVideosLoader extends AsyncTaskLoader<List<Video>> {

    private final Bundle args;
    private List<Video> videoList;
    private static final int VIDEOS = 4;
    private static final String LOG_TAG = FetchVideosLoader.class.getSimpleName();

    public FetchVideosLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        if (videoList == null) forceLoad();
        else deliverResult(videoList);
    }

    @Override
    public List<Video> loadInBackground() {

        String movieId = args.getString(getContext().getString(R.string.MOVIE_ID));

        URL movieRequestUrl = NetworkUtils.buildUrl(VIDEOS, movieId);

        try {
            String jsonVideoResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            Log.d(LOG_TAG, "loadInBackground: response " + jsonVideoResponse);
            return JsonVideoUtils.getVideoStringsFromJson(jsonVideoResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(final List<Video> videoData) {
        videoList = videoData;
        super.deliverResult(videoData);
    }

}
