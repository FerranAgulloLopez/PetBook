package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;
import service.main.services.SequenceGeneratorService;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForumComment {

    @Transient
    public static final String SEQUENCE_NAME = "forumComments_sequence";

    private long id;
    private String creatorMail;
    private Date creationDate;
    private Date updateDate;
    private String description;

    public ForumComment(String creatorMail, Date creationDate, String description) {
        this.creatorMail = creatorMail;
        this.creationDate = creationDate;
        this.description = description;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getDescription() {
        return description;
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

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /*
    Auxiliary operations
     */


}
