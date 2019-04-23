package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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


    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
    }

    public User(String email, String password, String firstName, String secondName, String dateOfBirth, String postalCode, boolean mailconfirmed) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
        this.firstName = firstName;
        this.secondName = secondName;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
        this.mailconfirmed = mailconfirmed;
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



    /*
    Auxiliary operations
     */

    public boolean checkPassword(String password_to_check) {
        return hasher.checkPassword(password_to_check.toCharArray(),password);
    }
}
