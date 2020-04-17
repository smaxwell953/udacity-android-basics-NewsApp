package com.example.udacity_android_basics_newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

import java.util.List;

import androidx.annotation.RequiresApi;

public class HealthLoader extends AsyncTaskLoader<List<Health>> {

    /** Tag for log messages */
    private static final String LOG_TAG = HealthLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public HealthLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public List<Health> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of health articles.
        return QueryUtils.fetchHealthData(mUrl);
    }
}
