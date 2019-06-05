package service.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import service.main.util.PBKDF2Hasher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "User", description = "An application user")
@Document(collection = "users")
public class User implements Serializable {

    static {
        hasher = new PBKDF2Hasher();
    }

    private static PBKDF2Hasher hasher;

    @Id private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String firstName;
    private String secondName;
    private String dateOfBirth;
    private String postalCode;
    private boolean mailconfirmed;
    private String foto;    // Esta encodeado en Base64(byte[]) y luego pasado a String[].
                            // Para conseguir la foto, pasar a Base64(byte[]) y luego convertir a file.
                            // File   -> byte[] -> String[]
                            // String[] -> byte[] -> File
    private Friend friends; // Amigos tanto los confirmados como lo que solicitan serlo.
    private String tokenFirebase;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WallPost> wallPosts;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role = "USER";
    @JsonIgnore
    private boolean banned = false;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String googleCalendarID;


    public User() {
        this.wallPosts = new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
        this.wallPosts = new ArrayList<>();

        friends = new Friend();
    }

    public User(String email, String password, String firstName, String secondName, String dateOfBirth, String postalCode, boolean mailconfirmed, String foto) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
        this.firstName = firstName;
        this.secondName = secondName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.mailconfirmed = mailconfirmed;
        this.foto = foto;
        this.wallPosts = new ArrayList<>();

        friends = new Friend();

    }

    public User(String email, String password, String firstName, String secondName, String dateOfBirth, String postalCode, boolean mailconfirmed) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
        this.firstName = firstName;
        this.secondName = secondName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.mailconfirmed = mailconfirmed;
        this.wallPosts = new ArrayList<>();

        friends = new Friend();
    }



    /*
    Get
     */

    public String getEmail() {return  email;}

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPostalCode() {
        return  postalCode;
    }

    public String getPassword() {
        return password;
    }

    public boolean isMailconfirmed() {
        return mailconfirmed;
    }

    public String getFoto() { return foto; }

    public Friend getFriends() { return friends; }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public String getRole() {
        return role;
    }

    public List<WallPost> getWallPosts() {
        return wallPosts;
    }

    public String getGoogleCalendarID() { return googleCalendarID; }

    public boolean isBanned() {
        return banned;
    }

    /*
    Set
     */

    public void setFirstName(String firstName) {
            this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = hasher.hash(password.toCharArray());
    }

    public void setMailconfirmed(boolean mailconfirmed) {
        this.mailconfirmed = mailconfirmed;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setFoto(String foto) { this.foto = foto; }

    public void setFriends(Friend friends) { this.friends = friends; }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public void setWallPosts(List<WallPost> wallPosts) {
        this.wallPosts = wallPosts;
    }

    public void setGoogleCalendarID(String googleCalendarID) { this.googleCalendarID = googleCalendarID; }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    /*
    Friends
     */

    public void addFriend(String friend) {
        friends.addFriend(friend);
    }

    public void removeFriend(String friend) {
        friends.removeFriend(friend);
    }

    public boolean isFriend(String friend)  {
        return friends.isFriend(friend);
    }

    public void addFriendRequest(String friend) {
        friends.addFriendRequest(friend);
    }

    public void removeFriendRequest(String friend) {
        friends.removeFriendRequest(friend);
    }

    public boolean beenRequestedToBeFriendBy(String friend)  {
        return friends.beenRequestedToBeFriendBy(friend);
    }


     /*
    Friends Suggestion
     */

    public void addRejectedUserSuggestion(String userEmail) {
        friends.addRejectedUserSuggestion(userEmail);
    }

    public void removeRejectedUserSuggestion(String userEmail) {
        friends.removeRejectedUserSuggestion(userEmail);
    }

    public boolean rejectedFriendSuggestionOf(String userEmail)  { return friends.rejectedFriendSuggestionOf(userEmail); }



    /*
    Auxiliary operations
     */

    public boolean checkPassword(String password_to_check) {
        return hasher.checkPassword(password_to_check.toCharArray(),password);
    }

    public void addWallPost(WallPost wallPost) {
        this.wallPosts.add(wallPost);
    }

    public boolean deleteWallPost(long wallPostId) {
        boolean found = false;
        for (int i = 0; !found && i < wallPosts.size(); ++i) {
            if (wallPosts.get(i).getId() == wallPostId) {
                found = true;
                wallPosts.remove(i);
            }
        }
        return found;
    }

    public WallPost findWallPost(long wallPostId) {
        boolean found = false;
        WallPost result = null;
        for (int i = 0; !found && i < wallPosts.size(); ++i) {
            if (wallPosts.get(i).getId() == wallPostId) {
                found = true;
                result = wallPosts.get(i);
            }
        }
        return result;
    }

    public WallPost findRetweet(long wallPostId) {
        boolean found = false;
        WallPost result = null;
        for (int i = 0; !found && i < wallPosts.size(); ++i) {
            if (wallPosts.get(i).getRetweetId() == wallPostId) {
                found = true;
                result = wallPosts.get(i);
            }
        }
        return result;
    }

    public boolean userAdmin() {
        return role.equals("ADMIN");
    }





}
