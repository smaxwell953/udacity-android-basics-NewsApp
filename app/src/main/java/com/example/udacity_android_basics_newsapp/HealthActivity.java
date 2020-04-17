package com.example.udacity_android_basics_newsapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HealthActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<List<Health>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = HealthActivity.class.getName();

    /** URL for health article data from the Guardian dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?q=health&api-key=test";

    /**
     * Constant value for the health loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int HEALTH_LOADER_ID = 1;

    /** Adapter for the list of health articles */
    private HealthAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView healthListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        healthListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of health articles as input
        mAdapter = new HealthAdapter(this, new ArrayList<Health>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        healthListView.setAdapter(mAdapter);

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected health article.
        healthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current health article that was clicked on
                Health currentHealth = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri healthUri = Uri.parse(currentHealth.getUrl());

                // Create a new intent to view the health article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, healthUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(HEALTH_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_min_date_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(HEALTH_LOADER_ID, null, this);
        }
    }

    @Override
    public android.content.Loader<List<Health>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minDate = sharedPrefs.getString(
                getString(R.string.settings_min_date_key),
                getString(R.string.settings_min_date_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minDate);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new HealthLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Health>> loader, List<Health> healtharticles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No health articles found."
        mEmptyStateTextView.setText(R.string.no_healtharticles);

        // Clear the adapter of previous health article data
        //mAdapter.clear();

        // If there is a valid list of {@link Health Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (healtharticles != null && !healtharticles.isEmpty()) {
            mAdapter.addAll(healtharticles);
            //updateUi(healtharticles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Health>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
