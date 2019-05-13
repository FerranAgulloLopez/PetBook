package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import service.main.exception.NotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "forums")
public class ForumThread implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "forumThread_sequence";

    @Id
    private long id;
    private String creatorMail;
    private Date creationDate;
    private Date updateDate;
    private String title;
    private String description;
    private String topic;
    private List<ForumComment> comments;

    public ForumThread() {
        this.comments = new ArrayList<>();
    }

    public ForumThread(String creatorMail, Date creationDate, String title, String description, String topic) {
        this.creatorMail = creatorMail;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.comments = new ArrayList<>();
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTopic() {
        return topic;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public List<ForumComment> getComments() {
        return comments;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    /*
    Auxiliary operations
     */

    public void addComment(ForumComment comment) {
        this.comments.add(comment);
    }

    public ForumComment findComment(long commentId) {
        ForumComment forumComment = null;
        boolean found = false;
        for (int i = 0; !found && i < comments.size(); ++i) {
            ForumComment comment = comments.get(i);
            if (comment.getId() == commentId) {
                forumComment = comment;
                found = true;
            }
        }
        return forumComment;
    }
}
