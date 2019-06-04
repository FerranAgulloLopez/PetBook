package com.example.PETBook;

import android.graphics.Bitmap;

public class SingletonUsuario {

    private static String Email;
    private String jwtToken;
    private Bitmap profilePicture;
    private Boolean mailConfirmed;
    private Boolean admin;

    private static SingletonUsuario ourInstance;

    public static SingletonUsuario getInstance() {
        if (ourInstance == null){
            ourInstance = new SingletonUsuario();
        }
        return ourInstance;
    }

    private SingletonUsuario() {
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public String getJwtToken() {
        return this.jwtToken;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail(){
        return Email;
    }

    public static void setEmail(String email){
        Email = email;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public boolean isMailConfirmed() {
        return mailConfirmed;
    }

    public void setMailConfirmed(Boolean mailConfirmed) {
        this.mailConfirmed = mailConfirmed;
        System.out.println("!!!MAILCONFIRMED: " + mailConfirmed);
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
