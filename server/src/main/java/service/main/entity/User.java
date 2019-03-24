package service.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "User", description = "An application user")
@Document(collection = "domain")
public class User implements Serializable {

    static {
        hasher = new PBKDF2Hasher();
    }

    private static PBKDF2Hasher hasher;

    @Id private String email;

    private String password;
    private String FirstName;
    private String SecondName;
    private String dateOfBirth;
    private String postalCode;


    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = hasher.hash(password.toCharArray());
    }

    public String getEmail() {return  email;}

    public String getFirstName() {
        return FirstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public String getDateOfBirth() {return dateOfBirth;}

    public String getPostalCode() {return  postalCode;}

    public void setFirstName(String firstName) { this.FirstName = firstName; }

    public void setPassword(String password) {
        this.password = hasher.hash(password.toCharArray());
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password_to_check) {
        return hasher.checkPassword(password_to_check.toCharArray(),password);
    }
}
