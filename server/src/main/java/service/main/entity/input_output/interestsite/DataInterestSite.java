package service.main.entity.input_output.interestsite;

import service.main.entity.input_output.DataLocalization;

import java.io.Serializable;

public class DataInterestSite implements Serializable {

    private String name;
    private DataLocalization localization;
    private String description;
    private String type;


    /*
    Get
     */

    public String getName() {
        return name;
    }

    public DataLocalization getLocalization() {
        return localization;
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
        return (name != null && localization.inputCorrect());
    }

}
