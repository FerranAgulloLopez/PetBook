package service.main.entity.input_output.ban;

import service.main.entity.Ban;

import java.io.Serializable;
import java.util.Date;

public class DataBan implements Serializable {

    private String suspectMail;
    private Date creationDate;
    private String description;

    /*
    Get
     */

    public String getSuspectMail() {
        return suspectMail;
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

    public boolean inputCorrect() {
        return (suspectMail != null && creationDate != null && description != null);
    }
}
