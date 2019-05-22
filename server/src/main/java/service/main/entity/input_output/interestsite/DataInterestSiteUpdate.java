package service.main.entity.input_output.interestsite;

import java.io.Serializable;

public class DataInterestSiteUpdate implements Serializable {

    private String name;
    private String description;
    private String type;


    /*
    Get
     */

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }


    /*
    Auxiliary operations
     */

    public boolean inputCorrect() {
        return (name != null || description != null || type != null);
    }
}
