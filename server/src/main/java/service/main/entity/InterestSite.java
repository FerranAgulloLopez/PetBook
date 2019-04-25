package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private boolean accepted;
    private String creatorMail;
    private List<String> votes;


    public InterestSite() {
        this.votes = new ArrayList<>();
    }

    public InterestSite(String name,
                        String localization)
    {
        this.name = name;
        this.localization = localization;
        this.votes = new ArrayList<>();
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
        this.accepted = false;
        this.creatorMail = creatorMail;
        this.votes = new ArrayList<>();
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
        this.accepted = accepted;
        this.creatorMail = creatorMail;
        this.votes = new ArrayList<>();
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

    public List<String> getVotes() {
        return votes;
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

    public void setVotes(List<String> votes) {
        this.votes = votes;
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

    public void addVote(String email) {
        votes.add(email);
        if (votes.size() > 5) accepted = true;
    }


}
