package com.example.android.newsapp2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private String mUrl;

    private static final String LOG_TAG = ArticleAdapter.class.getName();

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() is called...");
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() is called...");
        if (mUrl == null) {
            return null;
        }

        JsonExtractor extractor = new JsonExtractor();
        return extractor.fetchDataFrom(mUrl, getContext());
    }
}
