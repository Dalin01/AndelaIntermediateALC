package com.example.darlington.githubjavadev.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.darlington.githubjavadev.adapters.RecyclerViewAdapter;
import com.example.darlington.githubjavadev.api.Client;
import com.example.darlington.githubjavadev.api.Service;
import com.example.darlington.githubjavadev.R;
import com.example.darlington.githubjavadev.utilities.Item;
import com.example.darlington.githubjavadev.utilities.ItemResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //variables are declared and instantiated respectively
    private List<Item> myDevelopersProfiles;
    private Boolean loading = true;
    private RecyclerView recyclerView;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;
    private int pageCount = 1;
    private RecyclerViewAdapter mAdapter;
    LinearLayout no_internet_connection;
    LinearLayout progress;
    LinearLayout reload_progress_bar;
    LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initMyViews(); //This method is called that sets and initializes the corresponding recycler views.
        reload_progress_bar = (LinearLayout) findViewById(R.id.reload_id);
        myDevelopersProfiles = new ArrayList<>();

        //The swipe refresh layout is initialize and corresponding methods are set.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Reassign the value in page count to 1 and call a method that
                //refreshes the call and updates the view
                pageCount = 1;
                refreshData();
            }
        });
    }

    //Method that initialize the recycler view, calls required methods and listens for
    //scrolls.
    private void initMyViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    //Get the integer values of the no of items in the screen, the total item available and the
                    //items already seen and save them in the corresponding variables.
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    //If loading is true and the other conditions are met the boolean value in loading
                    //should be changed to false. Also, if the value in  pageCount is less than 8 fetch data and
                    //increment the value in pageCount
                    if (loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (pageCount < 8) {
                            pageCount = pageCount + 1;
                            reload_progress_bar.setVisibility(View.VISIBLE);//show the progress bar for reloading.
                            fetchData();
                        } else {
                            Toast.makeText(getApplicationContext(), "You have gotten to the end of the list", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });

        //Method is called that makes the network call.
        fetchData();
    }

    //Method that makes the network call and updates that view and list accordingly
    private void fetchData() {
        try {
            Client client = new Client();
            Service service =
                    client.getClient().create(Service.class);
            Call<ItemResponse> call = service.getItems(pageCount); //pass the value in page count as the query
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    progress = (LinearLayout) findViewById(R.id.progress_bar);
                    progress.setVisibility(View.GONE);
                    reload_progress_bar.setVisibility(View.GONE);
                    if (response != null) {
                        List<Item> items = response.body().getItems(); //Save the response in a list of items

                        myDevelopersProfiles.addAll(items); //Add that list to our defined list
                        //Make a check of the page number. If it is the first page create an instance of the adapter
                        //and set it before notifying it for changes else just notify the already created adapter for changes
                        if (pageCount == 1) {
                            mAdapter = new RecyclerViewAdapter(getApplicationContext(), myDevelopersProfiles);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        loading = true;
                    }
                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    //Display the "no internet connection layout" if there is a failure
                    progress = (LinearLayout) findViewById(R.id.progress_bar);
                    progress.setVisibility(View.GONE);
                    no_internet_connection = (LinearLayout) findViewById(R.id.no_internet_connection);
                    no_internet_connection.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //Refresh method that is called on Swipe Refresh
    private void refreshData() {
        try {
            Client client = new Client();
            Service service =
                    client.getClient().create(Service.class);
            Call<ItemResponse> call = service.getItems(pageCount);
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    if(response != null){
                        List<Item> items = response.body().getItems();
                        myDevelopersProfiles.clear();
                        myDevelopersProfiles.addAll(items);
                        if(mAdapter != null){
                            mAdapter.notifyDataSetChanged();
                        }
                        else{
                            no_internet_connection.setVisibility(View.GONE);
                            mAdapter = new RecyclerViewAdapter(getApplicationContext(), myDevelopersProfiles);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to Refresh..", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}






