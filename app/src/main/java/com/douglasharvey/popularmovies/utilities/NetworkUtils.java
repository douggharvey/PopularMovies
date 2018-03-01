package com.douglasharvey.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.douglasharvey.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String MOVIES_BASE_URL =
            "http://api.themoviedb.org/3/movie/";

    private static final String TOP_RATED =
            "top_rated";

    private static final String POPULAR =
            "popular";

    private static final String REVIEWS =
            "reviews";

    private static final String VIDEOS =
            "videos";

    private static final String API_KEY_EXTENSION =
            "api_key";

    private static final String API_KEY = BuildConfig.API_KEY;

    public static URL buildUrl(int queryType, String movieId) {
        String queryTypeString = TOP_RATED;
        switch (queryType) {
            case 1:
                queryTypeString = TOP_RATED;
                break;
            case 2:
                queryTypeString = POPULAR;
                break;
            case 3:
                queryTypeString = movieId + "/" + REVIEWS;
                break;
            case 4:
                queryTypeString = movieId + "/" + VIDEOS;
                break;
        }
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendEncodedPath(queryTypeString)
                .appendQueryParameter(API_KEY_EXTENSION, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d("NetworkUtils", "buildUrl: " + builtUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildFullPath(String path) {
        // Note: w185 size recommended for most phones in implementation guide
        String Url = "http://image.tmdb.org/t/p/w780/";
        return Url + path;
    }

    // Reference: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("ConstantConditions") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
