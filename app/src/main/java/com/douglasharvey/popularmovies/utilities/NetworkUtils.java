package com.douglasharvey.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

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

    private static final String API_KEY_EXTENSION =
            "api_key";

    private static final String API_KEY = BuildConfig.API_KEY;

    public static URL buildUrl(int queryType) {
        String queryTypeString;
        switch (queryType) {
            case 1:
                queryTypeString = TOP_RATED;
                break;
            case 2:
                queryTypeString = POPULAR;
                break;
            default:
                queryTypeString = TOP_RATED;
                break;
        }
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(queryTypeString)
                .appendQueryParameter(API_KEY_EXTENSION, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
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

    public static String buildPosterFullPath(String posterPath) {
        // Note: w185 size recommended for most phones in implementation guide
        String postersURL = "http://image.tmdb.org/t/p/w185/";
        return postersURL + posterPath;
    }

    public static boolean isInternetAvailable(Context context) {
        boolean connectivityResult = false;
        if (isNetworkAvailable(context)) {
            if (isOnline()) {
                connectivityResult = true;
            }
        }
        return connectivityResult;
    }

// Reference: https://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity

    private static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("ConstantConditions") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
