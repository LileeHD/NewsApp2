package com.example.android.newsapp2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonExtractor {
    private static final String LOG_TAG = JsonExtractor.class.getName();

    public List<Article> fetchDataFrom(String requestUrl, Context context) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(LOG_TAG, "TEST: JsonExtractor fetchDataFrom() called");
        String jsonResponse = null;

        try {
            jsonResponse = NetworkingProcess.makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private List<Article> extractFeatureFromJson(String JsonArticle) {
        // Create an empty ArrayList that we can start adding news to
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(JsonArticle);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray articlesArray = response.getJSONArray("results");

//          get object from each array element
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject currentArticle = articlesArray.getJSONObject(i);
                String section = currentArticle.getString("sectionName");
                String title = currentArticle.getString("webTitle");
                String url = currentArticle.getString("webUrl");

//              Ok I suppose it's not the best way to format Date and Time. Let me know :-)
                String date = currentArticle.getString("webPublicationDate").replace("T", " ").replace("Z", " ");

//              Trying to retrieve the author, not really sure about the following lines.
                JSONObject fields = currentArticle.getJSONObject("fields");
                String author = fields.getString("byline");

                Article articleDetails = new Article(section, title, url, date, author);
                articles.add(articleDetails);
            }

        } catch (JSONException e) {
            Log.e("JsonExtractor", "Problem parsing the JSON results", e);
        }

        // Return the list of earthquakes
        return articles;
    }

}
