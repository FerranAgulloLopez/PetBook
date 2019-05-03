package service.main.entity.input_output.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import service.main.entity.Event;
import service.main.entity.input_output.DataLocalization;

import java.io.Serializable;
import java.util.Date;

public class DataEvent implements Serializable {

    private String creatorMail;
    private DataLocalization localization;
    private Date date;
    private String title;
    private String description;
    @JsonProperty
    private boolean isPublic;


    /*
    Get
     */

    public String getCreatorMail() {
        return creatorMail;
    }

    public DataLocalization getLocalization() {
        return localization;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }


    /*
    Auxiliary operations
     */

    public Event toEvent() {
        return new Event(creatorMail,localization.toLocalization(),date,title,description,isPublic);
    }

}