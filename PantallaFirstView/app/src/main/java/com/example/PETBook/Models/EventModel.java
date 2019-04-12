package com.example.PETBook.Models;

public class EventModel {

    private String localizacion;
    private String fecha;

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getLocalizacion() {
        return localizacion;
    }
}
