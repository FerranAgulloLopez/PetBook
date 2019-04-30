package service.main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ForumComment {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String creatorMail;
    private Date creationDate;
    private String description;

    public ForumComment(String creatorMail, Date creationDate, String description) {
        this.creatorMail = creatorMail;
        this.creationDate = creationDate;
        this.description = description;
    }


    /*
    Get
     */

    public String getId() {
        return id;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }


    /*
    Set
     */

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /*
    Auxiliary operations
     */

    private void makeId() {
        this.id = creatorMail + creationDate.getTime();
    }
}
