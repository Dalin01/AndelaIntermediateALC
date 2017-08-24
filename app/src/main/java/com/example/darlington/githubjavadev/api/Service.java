package com.example.darlington.githubjavadev.api;

import com.example.darlington.githubjavadev.constants.Url;
import com.example.darlington.githubjavadev.utilities.ItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DARLINGTON on 8/23/2017.
 */

public interface Service {

    //Service interface required to define the end points.
    @GET(Url.SEARCH_URL)
    Call<ItemResponse> getItems(@Query("page") int page);
}
