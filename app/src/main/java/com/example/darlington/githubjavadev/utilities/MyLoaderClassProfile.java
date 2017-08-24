package com.example.darlington.githubjavadev.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by DARLINGTON on 7/25/2017.
 */

public class MyLoaderClassProfile extends AsyncTaskLoader<List<DevelopersProfile>> {

    // get the name of the class that will be used for logging errors
    private static final String LOG_TAG = MyLoaderClassProfile.class.getName();

    // The URL that will be queried for the JSON
    //gotten from GitHub
    private String urls = "";

    //constructor method that is called when an instance of the class is created
    // it takes in 2 arguments - the context which the MainActivity and the url
    public MyLoaderClassProfile(Context context, String url) {
        super(context);
        urls = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<DevelopersProfile> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (urls == null) {
            return null;
        }
        List<DevelopersProfile> result = QueryUtilsProfile.fetchDevelopersProfile(urls);
        return result;
    }
}
