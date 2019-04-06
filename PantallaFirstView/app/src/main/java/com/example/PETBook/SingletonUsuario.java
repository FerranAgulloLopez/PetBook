package com.example.PETBook;

public class SingletonUsuario {

    private static String Email;

    private static SingletonUsuario ourInstance;

    public static SingletonUsuario getInstance() {
        if (ourInstance == null){
            ourInstance = new SingletonUsuario();
        }
        return ourInstance;
    }

    private SingletonUsuario() {
    }

    public String getEmail(){
        return Email;
    }

    public static void setEmail(String email){
        Email = email;
    }
}
