package service.main.entity.input_output.user;

import java.io.Serializable;
import java.util.Date;

public class DataWallPost implements Serializable {

    private String description;
    private Date creationDate;


    /*
    Get
     */

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    /*
    Auxiliary operations
     */

    public boolean isOk() {
        return !(this.description == null || this.creationDate == null);
    }
}
