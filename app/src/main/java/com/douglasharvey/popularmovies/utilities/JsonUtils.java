package com.douglasharvey.popularmovies.utilities;

import android.util.Log;

import com.douglasharvey.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static List<Movie> getMovieStringsFromJson(String movieJsonStr) throws JSONException {

        final String JSON_STATUS_CODE = "status_code";
        final String JSON_RESULTS = "results";

        final String JSON_TITLE = "title";
        final String JSON_RELEASE_DATE = "release_date";
        final String JSON_POSTER_PATH = "poster_path";
        final String JSON_VOTE_AVERAGE = "vote_average";
        final String JSON_OVERVIEW = "overview";

        ArrayList<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(JSON_STATUS_CODE)) {
            int errorCode = movieJson.getInt(JSON_STATUS_CODE);
            Log.e(LOG_TAG, "JSON Parse error: " + errorCode);
        }
        else {
            JSONArray movieArray = movieJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject resultObject = movieArray.getJSONObject(i);
                String title = resultObject.optString(JSON_TITLE);
                String release_date = resultObject.optString(JSON_RELEASE_DATE);
                String overview = resultObject.optString(JSON_OVERVIEW);
                String posterPath = resultObject.optString(JSON_POSTER_PATH);
                String voteAverage = resultObject.optString(JSON_VOTE_AVERAGE);

                parsedMovieData.add(new Movie(title, release_date, posterPath, voteAverage, overview));
            }
        }
        return parsedMovieData;
    }
}
