package service.main.entity.input_output.forum;

import service.main.entity.ForumThread;

import java.io.Serializable;
import java.util.Date;

public class DataForumThread implements Serializable {

    private Date creationDate;
    private String title;
    private String description;
    private String topic;


    /*
    Get
     */

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

}
