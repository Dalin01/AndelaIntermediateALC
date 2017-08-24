package com.example.darlington.githubjavadev.utilities;

/**
 * Created by DARLINGTON on 7/24/2017.
 */

//A utility class intended to create instances of a developer'
//profile details that will be passed to the adapter and hence
//populated in the list.
public class DevelopersProfile {

    //declaration private variable
    private String name;
    private String html_url;
    private String followers;
    private String following;
    private String bio;
    private String repos;
    private String company;
    private String location;
    private String status;

    //public constructor that is called when an instance of the class is
    //created. It takes in 9 arguments
    public DevelopersProfile(String name, String html_url,
                             String followers, String following, String bio,
                             String repos, String company, String location, String status) {
        this.name = name;
        this.html_url = html_url;
        this.followers = followers;
        this.following = following;
        this.bio = bio;
        this.repos = repos;
        this.company = company;
        this.location = location;
        this.status = status;
    }

    //getter methods.
    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getCompany() {
        return company;
    }

    public String getFollowers() {
        return followers;
    }

    public String getFollowing() {
        return following;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getRepos() {
        return repos;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

}
