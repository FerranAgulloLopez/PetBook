package com.example.PETBook.Models;

import java.io.Serializable;

public class PetModel implements Serializable {

    private String id;
    private String nombre;
    private String userEmail;
    private String especie;
    private String raza;
    private String sexo;
    private String descripcion;
    private String foto;
    private String color;
    private String edad;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getColor() {
        return color;
    }

    public String getEdad() {
        return edad;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
