package service.main.entity.input_output.user;

import io.swagger.annotations.ApiModelProperty;

public class OutUpdateUserProfile {

    private String firstName;
    private String secondName;
    private String dateOfBirth;
    private String postalCode;
    private String password;


    public OutUpdateUserProfile() {}

    public String getFirstName() {
        return this.firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getPostalCode() {
        return  this.postalCode;
    }

    public String getPassword() {
        return password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
