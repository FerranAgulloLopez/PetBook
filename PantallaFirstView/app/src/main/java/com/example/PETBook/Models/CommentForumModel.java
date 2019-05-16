package com.example.PETBook.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentForumModel implements Serializable {
    private String creationDate;
    private String creatorMail;
    private String description;
    private Integer IDComment;


    public Integer getIDComment() {
        return IDComment;
    }

    public void setIDComment(Integer IDComment) {
        this.IDComment = IDComment;
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


}
