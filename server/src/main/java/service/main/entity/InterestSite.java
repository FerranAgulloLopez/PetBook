package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "interestsites")
public class InterestSite implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "interestSites_sequence";

    @Id
    private long id;
    private String name;
    private Localization localization;
    private String description;
    private String type;
    private boolean accepted;
    private String creatorMail;
    private List<String> votes;


    public InterestSite() {
        this.votes = new ArrayList<>();
    }

    public InterestSite(String name,
                        Localization localization,
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
    }


    /*
    Get
     */

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Localization getLocalization() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
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

    public void addVote(String email) {
        votes.add(email);
        if (votes.size() > 4)  {
            accepted = true;
            votes = null;
        }
    }

    public void deleteVote(String email) {
        votes.remove(email);
    }


}
