package com.example.PETBook.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class WallModel implements Serializable {
    private String description;
    private String creationDate;
    private Integer IDWall;
    private ArrayList<String> likes;
    private ArrayList<String> favs;
    private ArrayList<String> retweets;


    public Integer getIDWall() {
        return IDWall;
    }

    public void setIDWall(Integer IDWall) {
        this.IDWall = IDWall;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getFavs() {
        return favs;
    }

    public void setFavs(ArrayList<String> favs) {
        this.favs = favs;
    }

    public ArrayList<String> getRetweets() {
        return retweets;
    }

    public void setRetweets(ArrayList<String> retweets) {
        this.retweets = retweets;
    }
}
