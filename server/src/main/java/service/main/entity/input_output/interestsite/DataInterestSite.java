package service.main.entity.input_output.interestsite;

import service.main.entity.InterestSite;

import java.io.Serializable;

public class DataInterestSite implements Serializable {

    private String name;
    private String localization;
    private String description;
    private String type;
    private String creatorMail;


    /*
    Get
     */

    public String getName() {
        return name;
    }

    public String getLocalization() {
        return localization;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getCreatorMail() {
        return creatorMail;
    }


    /*
    Auxiliary operations
     */

    public InterestSite toInterestSite() {
        return new InterestSite(name,localization,description,type,creatorMail);
    }
}
