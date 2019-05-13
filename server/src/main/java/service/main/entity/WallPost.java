package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WallPost implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "wallPosts_sequence";

    private long id;
    private String description;
    private Date creationDate;
    private Date updateDate;

    public WallPost(String description, Date creationDate, Date updateDate) {
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
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
}
