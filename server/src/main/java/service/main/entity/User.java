package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import service.main.util.PBKDF2Hasher;

import java.io.Serializable;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role = "USER";


    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());

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



}
