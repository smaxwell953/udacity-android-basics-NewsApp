package com.example.udacity_android_basics_newsapp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link Health} objects.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Health> fetchHealthData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Health Article}s

        // Return the list of {@link Health Article}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the health article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Health> extractFeatureFromJson(String healthJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(healthJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding health articles to
        List<Health> healtharticles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(healthJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or health articles).
            JSONArray healthArray = baseJsonResponse.getJSONArray("response");

            // For each health article in the healthArray, create an {@link Health} object
            for (int i = 0; i < healthArray.length(); i++) {

                // Get a single healtharticle at position i within the list of health articles
                JSONObject currentHealth = healthArray.getJSONObject(i);

                // For a given health article, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that health article.
                JSONObject properties = currentHealth.getJSONObject("id");

                // Extract the value for the key called "type"
                String type = properties.getString("type");

                // Extract the value for the key called "webTitle"
                String webTitle = properties.getString("webTitle");

                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = properties.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String webUrl = properties.getString("webUrl");

                // Create a new {@link Health} object with the type, title, date,
                // and url from the JSON response.
                Health healthnews = new Health(type, webTitle, webPublicationDate, webUrl);

                // Add the new {@link Health} to the list of health articles.
                healtharticles.add(healthnews);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the health article JSON results", e);
        }

        // Return the list of health articles
        return healtharticles;
    }

}

