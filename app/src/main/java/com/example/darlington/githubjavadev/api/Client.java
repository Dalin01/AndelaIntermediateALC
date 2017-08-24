package com.example.darlington.githubjavadev.api;

import com.example.darlington.githubjavadev.constants.Url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DARLINGTON on 8/23/2017.
 */

public class Client {

    //Variable is declared and initialized.
    private static Retrofit retrofit = null;

    //The get client method that returns a retrofit obj is created
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
