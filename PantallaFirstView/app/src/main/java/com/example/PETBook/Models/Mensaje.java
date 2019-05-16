package com.example.PETBook.Models;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    private String mensaje;
    private String urlFoto;
    private boolean contineFoto;
    private String emailCreador;
    private Object createdTimestamp;


    public Mensaje() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }




    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean isContineFoto() {
        return contineFoto;
    }

    public void setContineFoto(boolean contineFoto) {
        this.contineFoto = contineFoto;
    }

    public String getEmailCreador() {
        return emailCreador;
    }

    public void setEmailCreador(String emailCreador) {
        this.emailCreador = emailCreador;
    }


    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }
}
