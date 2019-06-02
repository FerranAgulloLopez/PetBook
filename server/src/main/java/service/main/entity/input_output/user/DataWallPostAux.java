package service.main.entity.input_output.user;

import service.main.entity.WallPost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataWallPostAux implements Serializable, Comparable<DataWallPostAux> {

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
    private String creatorMail;
    private String foto;

    public DataWallPostAux(WallPost aux, String creatorMail, String foto) {
        this.id = aux.getId();
        this.description = aux.getDescription();
        this.creationDate = aux.getCreationDate();
        this.updateDate = aux.getUpdateDate();
        this.retweet = aux.isRetweet();
        this.retweetId = aux.getRetweetId();
        this.retweetText = aux.getRetweetText();
        this.likes = aux.getLikes();
        this.loves = aux.getLoves();
        this.retweets = aux.getRetweets();
        this.creatorMail = creatorMail;
        this.foto = foto;
    }

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

    public String getCreatorMail() {
        return creatorMail;
    }

    public String getFoto() {
        return foto;
    }

    @Override
    public int compareTo(DataWallPostAux o) {
        return this.getCreationDate().compareTo(o.getCreationDate());
    }
}
