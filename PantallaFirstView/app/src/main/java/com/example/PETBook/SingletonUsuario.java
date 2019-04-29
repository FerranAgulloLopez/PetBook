package com.example.PETBook;

import android.graphics.Bitmap;

public class SingletonUsuario {

    private static String Email;
    private Bitmap profilePicture;

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

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail(){
        return Email;
    }

    public static void setEmail(String email){
        Email = email;
    }
}
