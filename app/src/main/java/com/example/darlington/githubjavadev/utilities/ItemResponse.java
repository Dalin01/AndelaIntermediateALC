package com.example.darlington.githubjavadev.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DARLINGTON on 8/23/2017.
 */

public class ItemResponse {

    //Variables are declared
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    //method that returns a list of type items.
    public List<Item> getItems() {
        return items;
    }


}