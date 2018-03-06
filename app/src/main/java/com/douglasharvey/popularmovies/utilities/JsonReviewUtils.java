package com.douglasharvey.popularmovies.utilities;

import android.util.Log;

import com.douglasharvey.popularmovies.data.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class JsonReviewUtils {
    private static final String LOG_TAG = JsonReviewUtils.class.getSimpleName();

    static List<Review> getReviewStringsFromJson(String reviewJsonStr) throws JSONException {

        final String JSON_STATUS_CODE = "status_code";
        final String JSON_RESULTS = "results";

        final String JSON_ID = "id";
        final String JSON_AUTHOR = "author";
        final String JSON_CONTENT = "content";
        final String JSON_URL = "url";

        ArrayList<Review> parsedReviewData = new ArrayList<>();

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        if (reviewJson.has(JSON_STATUS_CODE)) {
            int errorCode = reviewJson.getInt(JSON_STATUS_CODE);
            Log.e(LOG_TAG, "JSON Parse error: " + errorCode);
        }
        else {
            JSONArray reviewArray = reviewJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject resultObject = reviewArray.getJSONObject(i);

                String id = resultObject.optString(JSON_ID);
                String author = resultObject.optString(JSON_AUTHOR);
                String content = resultObject.optString(JSON_CONTENT);
                String url = resultObject.optString(JSON_URL);

                parsedReviewData.add(new Review(id, author, content, url));
            }
        }
        return parsedReviewData;
    }
}
