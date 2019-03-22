package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "domain")
public class User implements Serializable {

    @Id private String email;

    private String password;
    private String FirstName;
    private String SecondName;
    private String dateOfBirth;
    private String postalCode;


    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
