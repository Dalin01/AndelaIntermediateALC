package com.example.darlington.githubjavadev.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by DARLINGTON on 8/22/2017.
 */

public class Item {

    //variables are declared
    @SerializedName("login")
    @Expose
    private String userName;

    @SerializedName("avatar_url")
    @Expose
    private String imageUrl;

    //constructor that sets the variables and initializes them
    public Item(String userName, String imageUrl){
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    //getter methods returning the values in userName and the imageUrl
    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
