package com.example.PETBook.Models;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class EventModel implements Serializable {


    private String direccion;
    private Pair<Double, Double> Coordenadas;
    private String fecha;
    private String titulo;
    private String descripcion;
    private boolean publico;
    private ArrayList<String> miembros;
    private String creador;

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
        Coordenadas = new Pair<>(Long,Lat);
    }

    public Pair<Double,Double> getCoordenadas() { return Coordenadas; }

    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }

    public String getDescripcion(){ return descripcion; }

    public void setPublico(boolean publico){ this.publico = publico; }

    public boolean getPublic(){ return publico; }

    public void setMiembros(ArrayList<String> miembros){ this.miembros = miembros; }

    public ArrayList<String> getMiembros(){ return miembros; }

    public void setCreador(String creador){ this.creador = creador; }

    public String getCreador(){ return creador; }
}
