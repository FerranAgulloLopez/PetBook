package service.main.entity.input_output.forum;


import java.util.Date;

public class DataForumCommentUpdate {

    private String description;
    private Date updateDate;


    /*
    Get
     */

    public String getDescription() {
        return description;
    }

    public Date getUpdateDate() {
        return updateDate;
    }
}
