package com.example.PETBook.Models;

import java.io.Serializable;

public class WallModel implements Serializable {
    private String description;
    private String creationDate;
    private Integer IDWall;

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
}
