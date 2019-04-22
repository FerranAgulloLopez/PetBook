package com.example.PETBook.Models;

import java.io.Serializable;

public class EventModel implements Serializable {


    private Integer localizacion;
    private String fecha;
    private String titulo;
    private String descripcion;
    private boolean publico;

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

    public void setLocalizacion(Integer localizacion){ this.localizacion = localizacion; }

    public Integer getLocalizacion(){ return localizacion;}

    public void setDescripcion(String descripcion){ this.descripcion = descripcion; }

    public String getDescripcion(){ return descripcion; }

    public void setPublico(boolean publico){ this.publico = publico; }

    public boolean getPublic(){ return publico; }
}
