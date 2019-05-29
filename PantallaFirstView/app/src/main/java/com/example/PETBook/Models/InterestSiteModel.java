package com.example.PETBook.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class InterestSiteModel implements Serializable {

    private Integer id;
    private String titulo;
    private String tipo;
    private String descripcion;
    private String direccion;
    private Double latitude;
    private Double longitude;
    private ArrayList<String> votantes;



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<String> getVotantes() {
        return votantes;
    }

    public void setVotantes(ArrayList<String> votantes) {
        this.votantes = votantes;
    }
}
