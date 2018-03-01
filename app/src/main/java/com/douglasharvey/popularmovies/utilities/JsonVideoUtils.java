package com.douglasharvey.popularmovies.utilities;

import android.util.Log;

import com.douglasharvey.popularmovies.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class JsonVideoUtils {
    private static final String LOG_TAG = JsonVideoUtils.class.getSimpleName();

    static List<Video> getVideoStringsFromJson(String videoJsonStr) throws JSONException {

        final String JSON_STATUS_CODE = "status_code";
        final String JSON_RESULTS = "results";

        final String JSON_ID = "id";
        final String JSON_KEY = "key";
        final String JSON_NAME = "name";
        final String JSON_TYPE = "type";

        ArrayList<Video> parsedVideoData = new ArrayList<>();

        JSONObject videoJson = new JSONObject(videoJsonStr);

        if (videoJson.has(JSON_STATUS_CODE)) {
            int errorCode = videoJson.getInt(JSON_STATUS_CODE);
            Log.e(LOG_TAG, "JSON Parse error: " + errorCode);
        }
        else {
            JSONArray videoArray = videoJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < videoArray.length(); i++) {
                JSONObject resultObject = videoArray.getJSONObject(i);

                String id = resultObject.optString(JSON_ID);
                String key = resultObject.optString(JSON_KEY);
                String name = resultObject.optString(JSON_NAME);
                String type = resultObject.optString(JSON_TYPE);

                parsedVideoData.add(new Video(id, key, name, type));
            }
        }
        return parsedVideoData;
    }
}
