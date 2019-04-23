package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "interestsites")
public class InterestSite implements Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;

    private String name;
    private String localization;

    private String description;
    private String type;
    private int votesNumber;
    private boolean accepted;
    private String creatorMail;

    public InterestSite() {}

    public InterestSite(String name,
                        String localization)
    {
        this.name = name;
        this.localization = localization;
        makeId();
    }

    public InterestSite(String name,
                        String localization,
                        String description,
                        String type,
                        String creatorMail)
    {
        this.name = name;
        this.localization = localization;
        this.description = description;
        this.type = type;
        this.votesNumber = 0;
        this.accepted = false;
        this.creatorMail = creatorMail;

        makeId();
    }

    public InterestSite(String name,
                        String localization,
                        String description,
                        String type,
                        int votesNumber,
                        boolean accepted,
                        String creatorMail)
    {
        this.name = name;
        this.localization = localization;
        this.description = description;
        this.type = type;
        this.votesNumber = votesNumber;
        this.accepted = accepted;
        this.creatorMail = creatorMail;

        makeId();
    }


    /*
    Get
     */

    public String getId() {
        return id;
    }

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

    public int getVotesNumber() {
        return votesNumber;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getCreatorMail() {
        return creatorMail;
    }


    /*
    Set
     */

    public void setName(String name) {
        this.name = name;
        makeId();
    }

    public void setLocalization(String localization) {
        this.localization = localization;
        makeId();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVotesNumber(int votesNumber) {
        this.votesNumber = votesNumber;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }


    /*
    auxiliary operations
    */
    private void makeId() {
        id = name + " " + localization;
    }


}
