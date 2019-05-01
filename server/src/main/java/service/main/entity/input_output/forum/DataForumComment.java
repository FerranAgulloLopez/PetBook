package service.main.entity.input_output.forum;

import service.main.entity.ForumComment;

import java.io.Serializable;
import java.util.Date;

public class DataForumComment implements Serializable {

    private String creatorMail;
    private Date creationDate;
    private String description;


    /*
    Get
     */

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
    Auxiliary operations
     */

    public ForumComment toComment() {
        return new ForumComment(creatorMail,creationDate,description);
    }


}
