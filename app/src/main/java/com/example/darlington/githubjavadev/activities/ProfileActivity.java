package com.example.darlington.githubjavadev.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.darlington.githubjavadev.constants.Url;
import com.example.darlington.githubjavadev.R;
import com.example.darlington.githubjavadev.utilities.DevelopersProfile;
import com.example.darlington.githubjavadev.utilities.MyLoaderClassProfile;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<DevelopersProfile>> {

    //declare views that are required
    TextView username, name, link, followers, following, bio, repos, company, location, status;
    ImageView profile_image;

    //declare variables
    String user;

    //declared used layouts
    NestedScrollView profile_view;
    LinearLayout progress;
    LinearLayout no_internet_connection;

    //the GitHub API that will be queried is passed into a variable that is declared static and final
    //so it can't be changed
    private String base_url = Url.BASE_URL + Url.USER;
    private String request_url;
    String my_link;

    private static final int DEVELOPERS_LOADER_ID = 1;
    String default_message_to_share = "Check out this awesome developer @";
    Typeface typeface_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB that calls an implicit intent that shares the developer's link.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, (default_message_to_share + user + ", " + my_link + "."));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //get the extra that was passed along with the intent from the MainActivity
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        request_url = base_url + user;
        String img_url = extras.getString("img_url");

        //find corresponding views using findViewById
        username = (TextView) findViewById(R.id.userName);
        name = (TextView) findViewById(R.id.name);
        link = (TextView) findViewById(R.id.link);
        followers = (TextView) findViewById(R.id.followers);
        following= (TextView) findViewById(R.id.following);
        bio = (TextView) findViewById(R.id.bio);
        repos = (TextView) findViewById(R.id.repos);
        company= (TextView) findViewById(R.id.company);
        location = (TextView) findViewById(R.id.location);
        status = (TextView) findViewById(R.id.status);
        TextView my_followers = (TextView) findViewById(R.id.my_followers);
        TextView my_following = (TextView) findViewById(R.id.my_followings);
        TextView my_repos = (TextView) findViewById(R.id.my_repos);
        TextView java = (TextView) findViewById(R.id.java);

        //set the text (user name of the developer) that was passed along with the intent
        username.setText(user);

        //set the fonts accordingly
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cooter_candy.ttf");
        typeface_java = Typeface.createFromAsset(getAssets(), "fonts/taco_bell.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/twinmarker.ttf");
        username.setTypeface(typeface);
        java.setTypeface(typeface_java);
        my_followers.setTypeface(typeface1);
        my_following.setTypeface(typeface1);
        my_repos.setTypeface(typeface1);


        //set the profile image using Glide
        profile_image = (ImageView) findViewById(R.id.profileImage);
        Glide.with(this)
                .load(img_url)
                .asBitmap()
                .placeholder(R.drawable.ic_placeholder)
                .into(profile_image);

        //get the network information
        //if it is not equal to null and is connected, call the initLoader method. This method
        //creates a new loader if none has been created before wrt the ID. otherwise, set the visibility
        //of the loader to gone and set the visibility for no internet connection to visible
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        no_internet_connection = (LinearLayout) findViewById(R.id.no_internet_connection);
        progress = (LinearLayout) findViewById(R.id.progress_indicator);
        if (networkInfo != null && networkInfo.isConnected())
        {
            getLoaderManager().initLoader(DEVELOPERS_LOADER_ID, null, this);
        }
        else
        {
            no_internet_connection.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }


    //this method is called when the initLoader is called for the first time
    //it takes in 2 arguments
    @Override
    public Loader<List<DevelopersProfile>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        MyLoaderClassProfile task = new MyLoaderClassProfile(this, request_url);
        return task;
    }

    //this method is called when the query is completed and the UI needs to be updated
    @Override
    public void onLoadFinished(Loader<List<DevelopersProfile>> loader, final List<DevelopersProfile> developersProfiles) {
        // TODO: Update the UI with the result
        // If there is a valid list of {@link Developers}, then add them to the adapter's
        // data set. This will trigger the recyclerView to update.
        if (developersProfiles != null && !developersProfiles.isEmpty()) {
            //set the right layout on the screen when the operation is successful
            profile_view = (NestedScrollView) findViewById(R.id.profile_view);
            profile_view.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            //set the views with the appropriate values or strings
            name.setText(developersProfiles.get(0).getName());
            following.setText(developersProfiles.get(0).getFollowing());
            followers.setText(developersProfiles.get(0).getFollowers());
            repos.setText(developersProfiles.get(0).getRepos());
            location.setText(developersProfiles.get(0).getLocation());

            //set the fonts.
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/petbone.ttf");
            Typeface typeface_link = Typeface.createFromAsset(getAssets(), "fonts/romans.ttf");
            Typeface typeface_bio = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.otf");
            name.setTypeface(typeface);
            following.setTypeface(typeface_link);
            followers.setTypeface(typeface_link);
            location.setTypeface(typeface_java);
            repos.setTypeface(typeface_link);

            String my_bio = developersProfiles.get(0).getBio();
            if(my_bio.equals("null")){
                bio.setText(R.string.not_provided);
                bio.setTypeface(typeface_bio);
            }
            else{
                bio.setText(developersProfiles.get(0).getBio());
                bio.setTypeface(typeface_bio);
            }

            String my_company = developersProfiles.get(0).getCompany();
            if(my_company.equals("null")){
                company.setText(R.string.not_provided);
                company.setTypeface(typeface_bio);
            }
            else{
                company.setText(developersProfiles.get(0).getCompany());
                company.setTypeface(typeface_bio);
            }

            String myStatus = developersProfiles.get(0).getStatus();
            if(myStatus.equals("null")){
                status.setText(R.string.undecided);
                status.setTypeface(typeface_bio);
            }
            else if(myStatus.equals("true")){
                status.setText(R.string.hireable);
                status.setTypeface(typeface_bio);
            }
            else{
                status.setText(R.string.not_hirable);
                status.setTypeface(typeface_bio);
            }

            my_link = developersProfiles.get(0).getHtml_url();
            //set the GitHub link is it is not equal to null or empty string
            //and then set the on click listener to the view he opens the browser
            if (my_link != ""){
                link.setVisibility(View.VISIBLE);
                link.setText(developersProfiles.get(0).getHtml_url());

                link.setTypeface(typeface_link);
                link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(my_link));
                        // Verify that the intent will resolve to an activity
                        if (i.resolveActivity(getPackageManager()) != null) {
                            startActivity(Intent.createChooser(i, "Choose an appropriate browser from..."));
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DevelopersProfile>> loader)
    {
    }
}
