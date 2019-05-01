package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import service.main.exception.NotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "forums")
public class ForumThread implements Serializable {

    //private static List<String> topics = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;
    private String creatorMail;
    private Date creationDate;
    private Date updateDate;
    private String title;
    private String description;
    private String topic;
    private List<ForumComment> comments;

    public ForumThread(String creatorMail, String title) {
        this.creatorMail = creatorMail;
        this.title = title;
        makeId();
    }

    public ForumThread(String creatorMail, Date creationDate, String title, String description, String topic) {
        this.creatorMail = creatorMail;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.comments = new ArrayList<>();
        makeId();
    }

    /*
    Get
     */

    /*public static List<String> getTopics() {
        return topics;
    }*/

    public String getId() {
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

    /*public static void addTopic(String topic) {
        topics.add(topic);
    }*/

    private void makeId() {
        this.id = creatorMail + title;
    }

    public void addComment(ForumComment comment) {
        this.comments.add(comment);
    }

    public ForumComment findComment(String commentId) {
        ForumComment forumComment = null;
        boolean found = false;
        for (int i = 0; !found && i < comments.size(); ++i) {
            ForumComment comment = comments.get(i);
            if (comment.getId().equals(commentId)) {
                forumComment = comment;
                found = true;
            }
        }
        return forumComment;
    }
}
