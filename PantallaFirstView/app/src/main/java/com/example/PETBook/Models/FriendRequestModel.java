package com.example.PETBook.Models;

import java.io.Serializable;

public class FriendRequestModel  implements Serializable {

    private String name;
    private String surnames;
    private String email;
    private String birthday;
    private String postalCode;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getSurnames() { return surnames; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() { return birthday; }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() { return postalCode; }


}
