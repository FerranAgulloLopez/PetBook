package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "events")
public class Event implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "events_sequence";

    @Id
    private long id;
    private String creatorMail;
    private Localization localization;
    private Date date;
    private String title;
    private String description;
    private boolean isPublic;
    private List<String> participants;

    public Event() {}

    public Event(String creatorMail,
                 Localization localization,
                 Date date,
                 String title,
                 String description,
                 boolean isPublic)
    {
        this.creatorMail = creatorMail;
        this.localization = localization;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.participants = new ArrayList<>();
        participants.add(creatorMail);
    }


    /*
    Get
     */

    public long getId() {
        return id;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public Localization getLocalization() {
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

    public List<String> getParticipants() {
        return participants;
    }


    /*
    Set
     */

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }


    /*
    Auxiliary operations
     */

    public boolean userParticipates(String userMail) {
        return participants.contains(userMail);
    }

    public void addParticipant(String participantMail) {
        participants.add(participantMail);
    }

    public void removePaticipant(String userMail) {
        participants.remove(userMail);
    }

}
