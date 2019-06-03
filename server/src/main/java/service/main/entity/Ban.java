package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ban implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "bans_sequence";

    @Id
    private long id;
    private String creatorMail;
    private String suspectMail;
    private String description;
    private List<String> rejected;
    private List<String> approved;
    private Date creationDate;
    private boolean closed;

    public Ban(String creatorMail, String suspectMail, String description, Date creationDate) {
        this.creatorMail = creatorMail;
        this.suspectMail = suspectMail;
        this.description = description;
        this.rejected = new ArrayList<>();
        this.approved = new ArrayList<>();
        this.creationDate = creationDate;
        this.closed = false;
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

    public String getSuspectMail() {
        return suspectMail;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRejected() {
        return rejected;
    }

    public List<String> getApproved() {
        return approved;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isClosed() {
        return closed;
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

    public void setSuspectMail(String suspectMail) {
        this.suspectMail = suspectMail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRejected(List<String> rejected) {
        this.rejected = rejected;
    }

    public void setApproved(List<String> approved) {
        this.approved = approved;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /*
    Auxiliary operations
     */

    public void addRejectVote(String userMail) {
        this.rejected.add(userMail);
    }

    public void addApprovedVote(String userMail) {
        this.approved.add(userMail);
    }

    public void removeVote(String userMail) {
        this.rejected.remove(userMail);
        this.approved.remove(userMail);
    }

    
}
