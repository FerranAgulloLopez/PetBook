package service.main.entity.input_output.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class DataWallPost implements Serializable {

    @JsonProperty(value="description")
    private String description;
    @JsonProperty(value="creationDate")
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

    public boolean inputCorrect() {
        return !(this.description == null || this.creationDate == null);
    }
}
