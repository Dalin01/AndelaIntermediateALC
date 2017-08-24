package com.example.darlington.githubjavadev.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.darlington.githubjavadev.activities.ProfileActivity;
import com.example.darlington.githubjavadev.R;
import com.example.darlington.githubjavadev.utilities.Item;

import java.util.List;

/**
 * Created by DARLINGTON on 7/24/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    //variables are declared
    private List<Item> developersProfiles;
    private Context context;

    //an inside class to the custom recycler adapter that extends form the
    //RecyclerView.ViewHolder class, it gets the views required and holds them
    //so the can be used/referenced when needed
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView a_user_name;
        public ImageView a_profile_image;

        //method that gets the views and sets them
        public MyViewHolder(View view) {
            super(view);
            a_user_name = (TextView) view.findViewById(R.id.user_name);
            a_profile_image = (ImageView) view.findViewById(R.id.profile_photo);
        }
    }

    //public constructor that takes in 2 arguments and sets the context and a list
    public RecyclerViewAdapter(Context context, List<Item> developersProfiles) {
        this.developersProfiles = developersProfiles;
        this.context = context;
    }

    //this method inflates the layout - custom_recycler_row and returns a view holder object
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recycler_row, parent, false);
        return new MyViewHolder(itemView);
    }

    //this method sets the appropriate date/values in the views - username and profile image
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Item a_dev_profile = developersProfiles.get(position);
        holder.a_user_name.setText(a_dev_profile.getUserName());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/cooter_candy.ttf");
        holder.a_user_name.setTypeface(typeface);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("img_url", a_dev_profile.getImageUrl());
                i.putExtra("user", a_dev_profile.getUserName());
                context.startActivity(i);
            }
        });

        //I used glide library to display the profile image
        Glide.with(context)
                .load(a_dev_profile.getImageUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.a_profile_image);
    }

    //returns the size of the arraylist
    @Override
    public int getItemCount() {
        return developersProfiles.size();
    }

}

