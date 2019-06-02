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
    private Integer retweetId;
    private boolean retweeted;
    private String retweetText;

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    private String creatorMail;
    private String foto;

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

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getRetweetText() {
        return retweetText;
    }

    public void setRetweetText(String reteetText) {
        this.retweetText = reteetText;
    }

    public Integer getRetweetId() {
        return retweetId;
    }

    public void setRetweetId(Integer retweetId) {
        this.retweetId = retweetId;
    }

}
