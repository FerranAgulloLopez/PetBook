package service.main.entity.input_output;

import service.main.entity.ForumThread;

import java.io.Serializable;
import java.util.Date;

public class DataForumThread implements Serializable {

    private String creatorMail;
    private Date creationDate;
    private String title;
    private String description;
    private String topic;


    /*
    Get
     */

    public String getCreatorMail() {
        return creatorMail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTopic() {
        return topic;
    }


    /*
    Auxiliary operations
     */

    public ForumThread toForum() {
        return new ForumThread(creatorMail,creationDate,title,description,topic);
    }
}
