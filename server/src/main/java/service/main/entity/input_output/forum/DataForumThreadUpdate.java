package service.main.entity.input_output.forum;

import java.io.Serializable;
import java.util.Date;

public class DataForumThreadUpdate implements Serializable {

    private Date updateDate;
    private String description;


    /*
    Get
     */

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getDescription() {
        return description;
    }

}