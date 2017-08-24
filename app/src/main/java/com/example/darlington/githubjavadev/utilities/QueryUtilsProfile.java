package com.example.darlington.githubjavadev.utilities;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DARLINGTON on 7/25/2017.
 */

public class QueryUtilsProfile {

    ///Log messages using the name of the class
    private static final String LOG_TAG = QueryUtilsProfile.class.getSimpleName();

    //empty constructor that is declared private. The purpose is such that an instance of this class
    //cannot be created
    private QueryUtilsProfile() {
    }

    // query the url and return a list of developersProfile objects.
    public static List<DevelopersProfile> fetchDevelopersProfile(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link DevelopersProfiles}s
        List<DevelopersProfile> developersProfiles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Developers}s
        return developersProfiles;
    }

    //returns a new url object from the given url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //Make an HTTP request to the given URL and return a String as the response.
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
            Log.e(LOG_TAG, "Problem retrieving the developer's profile JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Here, the string is parsed and necessary key-pairs are identified and the values extracted.
    //A list of DevelopersProfile is returned.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //The Json is parsed and the required values are retrieved from the key/value pairs.
    private static List<DevelopersProfile> extractFeatureFromJson(String developersJSON) {
        if (TextUtils.isEmpty(developersJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding our returned data to
        List<DevelopersProfile> developersProfiles = new ArrayList<>();
        try {
            JSONObject rootValue = new JSONObject(developersJSON);
            String name = rootValue.getString("name");
            String html_url = rootValue.getString("html_url");
            String followers = rootValue.getString("followers");
            String following = rootValue.getString("following");
            String bio = rootValue.getString("bio");
            String public_repos = rootValue.getString("public_repos");
            String company = rootValue.getString("company");
            String location = rootValue.getString("location");
            String hireable = rootValue.getString("hireable");
            DevelopersProfile developersProfile = new DevelopersProfile(name, html_url, followers, following, bio, public_repos,
                    company, location, hireable);
            developersProfiles.add(developersProfile);
        } catch (JSONException e) {
            Log.e("QueryUtilsProfile", "Problem parsing the earthquake JSON results", e);
        }
        return developersProfiles; // return the list of developers

    }
}
