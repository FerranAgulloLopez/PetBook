package service.main.entity.input_output.user;

import io.swagger.annotations.ApiModelProperty;
import service.main.entity.User;

import java.io.Serializable;

public class DataUser implements Serializable {

    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String dateOfBirth;
    private String postalCode;
    @ApiModelProperty(example = "false")
    private boolean mailconfirmed;

    /*
    Get
     */

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

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
        return postalCode;
    }

    public boolean isMailconfirmed() {
        return mailconfirmed;
    }

    /*
    Auxiliary operations
     */

    public User toUser() {
        return new User(email,password,firstName,secondName,dateOfBirth,postalCode,mailconfirmed);
    }
}
