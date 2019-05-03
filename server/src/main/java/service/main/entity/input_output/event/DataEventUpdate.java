package service.main.entity.input_output.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DataEventUpdate implements Serializable {

    private String title;
    private String description;
    @JsonProperty
    private boolean isPublic;


    /*
    Get
     */

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
