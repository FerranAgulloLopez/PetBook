package com.example.PETBook.Models;

import android.util.Pair;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventModel implements Serializable {


    private Integer id;
    private String direccion;
    private Double latitude;
    private Double longitude;
    private String fecha;
    private org.threeten.bp.LocalDate localDate;
    private String titulo;
    private String descripcion;
    private boolean publico;
    private ArrayList<String> miembros;
    private String creador;

    public void setId(Integer id) { this.id = id; }

    public Integer getId() { return id; }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() { return titulo; }

    public void setDireccion(String direccion){ this.direccion = direccion; }

    public String getDireccion(){ return direccion; }

    public void setCoordenadas(Double Long, Double Lat){
        latitude = Lat;
        longitude = Long;
    }

    public Double getLatitude(){ return latitude; }

    public Double getLongitude(){ return longitude; }

    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }

    public String getDescripcion(){ return descripcion; }

    public void setPublico(boolean publico){ this.publico = publico; }

    public boolean getPublic(){ return publico; }

    public void setMiembros(ArrayList<String> miembros){ this.miembros = miembros; }

    public ArrayList<String> getMiembros(){ return miembros; }

    public void setCreador(String creador){ this.creador = creador; }

    public String getCreador(){ return creador; }

    public Integer getNumeroParticipantes(){
        return miembros.size();
    }

    public org.threeten.bp.LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(org.threeten.bp.LocalDate localDate) {
        this.localDate = localDate;
    }
}
