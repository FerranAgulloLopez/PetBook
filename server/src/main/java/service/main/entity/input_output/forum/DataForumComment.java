package service.main.entity.input_output.forum;

import service.main.entity.ForumComment;

import java.io.Serializable;
import java.util.Date;

public class DataForumComment implements Serializable {

    private Date creationDate;
    private String description;


    /*
    Get
     */

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }


    /*
    Auxiliary operations
     */



}
