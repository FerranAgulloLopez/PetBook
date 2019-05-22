package service.main.entity.input_output.user;

import java.io.Serializable;
import java.util.Date;

public class DataWallPostUpdate implements Serializable {

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


    /*
    Auxiliary operations
     */

    public boolean inputCorrect() {
        return !(this.description == null || this.updateDate == null);
    }
}
