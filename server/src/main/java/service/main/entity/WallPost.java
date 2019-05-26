package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WallPost implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "wallPosts_sequence";

    private long id;
    private String description;
    private Date creationDate;
    private Date updateDate;
    private boolean retweet;
    private long retweetId;
    private String retweetText;
    private List<String> likes;
    private List<String> loves;
    private List<String> retweets;

    public WallPost() {
        this.likes = new ArrayList<>();
        this.loves = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    public WallPost(String description, Date creationDate, Date updateDate) {
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.likes = new ArrayList<>();
        this.loves = new ArrayList<>();
        this.retweets = new ArrayList<>();
        this.retweet = false;
    }


    /*
    Get
     */

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public boolean isRetweet() {
        return retweet;
    }

    public long getRetweetId() {
        return retweetId;
    }

    public String getRetweetText() {
        return retweetText;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<String> getLoves() {
        return loves;
    }

    public List<String> getRetweets() {
        return retweets;
    }

    /*
    Set
     */

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setRetweet(boolean retweet) {
        this.retweet = retweet;
    }

    public void setRetweetId(long retweetId) {
        this.retweetId = retweetId;
    }

    public void setRetweetText(String retweetText) {
        this.retweetText = retweetText;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public void setLoves(List<String> loves) {
        this.loves = loves;
    }

    public void setRetweets(List<String> retweets) {
        this.retweets = retweets;
    }

    /*
    Auxiliary operations
     */

    public boolean addLike(String userMail) {
        if (likes.contains(userMail)) return false;
        else {
            likes.add(userMail);
            return true;
        }
    }

    public boolean addLove(String userMail) {
        if (loves.contains(userMail)) return false;
        else {
            loves.add(userMail);
            return true;
        }
    }

    public boolean addRetweet(String userMail) {
        if (retweets.contains(userMail)) return false;
        else {
            retweets.add(userMail);
            return true;
        }
    }

    public boolean deleteLike(String userMail) {
        if (!likes.contains(userMail)) return false;
        else {
            likes.remove(userMail);
            return true;
        }
    }

    public boolean deleteLove(String userMail) {
        if (!loves.contains(userMail)) return false;
        else {
            loves.remove(userMail);
            return true;
        }
    }

    public boolean deleteRetweet(String userMail) {
        if (!retweets.contains(userMail)) return false;
        else {
            retweets.remove(userMail);
            return true;
        }
    }


}
