package com.example.android.newsapp2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>
        , SharedPreferences.OnSharedPreferenceChangeListener {

    String REQUEST_URL = "https://content.guardianapis.com/search?show-fields=byline&api-key=769d2c4f-44a6-48b8-9758-95d1a64a4de4";
//    "https://content.guardianapis.com/search?show-fields=byline&api-key=769d2c4f-44a6-48b8-9758-95d1a64a4de4"

    private ArticleAdapter mAdapter;
    TextView mEmptyView;

    private static final int LOADER_ID = 1;

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = (ListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyView);

        mAdapter = new ArticleAdapter(this, R.layout.list_item, new ArrayList<Article>());
        articleListView.setAdapter(mAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article currentArticle = mAdapter.getItem(i);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(intent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        }

        if (activeNetwork != null && activeNetwork.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "TEST: calling initLoader()...");

            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            mEmptyView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() is called...");
//        return new ArticleLoader(this, REQUEST_URL);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Strings value order by menu
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        // Strings value edition menu
        String edition = sharedPreferences.getString(
                getString(R.string.settings_edition_key),
                getString(R.string.settings_order_by_default)
        );
//        Parsing
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

//        Append queries
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("production-office", edition);

//        Result
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() is called...");

        mEmptyView.setText(R.string.no_news);

        mAdapter.clear();
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() is called...");
        mAdapter.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.settings_order_by_key)) ||
                key.equals(getString(R.string.settings_edition_key))) {
            mAdapter.clear();
            mEmptyView.setVisibility(View.GONE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
