package com.example.PETBook.Models;

import org.w3c.dom.Comment;

import java.io.Serializable;
import java.util.ArrayList;

public class ForumModel implements Serializable {
    private String creationDate;
    private String creatorMail;
    private String description;
    private String title;
    private String topic;
    private ArrayList<CommentForumModel> comments;
    private Integer IDForum;

    public Integer getIDForum() {
        return IDForum;
    }

    public void setIDForum(Integer IDForum) {
        this.IDForum = IDForum;
    }


    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<CommentForumModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentForumModel> comments) {
        this.comments = comments;
    }
}
