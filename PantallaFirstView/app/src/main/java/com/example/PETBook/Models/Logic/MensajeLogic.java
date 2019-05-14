package com.example.PETBook.Models.Logic;

import com.example.PETBook.Models.Mensaje;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MensajeLogic {

    private String key;
    private Mensaje mensaje;
    private String emailCreador;


    public MensajeLogic(String key, Mensaje mensaje) {
        this.key = key;
        this.mensaje = mensaje;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmailCreador() {
        return emailCreador;
    }

    public void setEmailCreador(String emailCreador) {
        this.emailCreador = emailCreador;
    }

    public long getCreatedTimestampLong() {
        return (long) mensaje.getCreatedTimestamp();
    }

    public String fechaDeCreacionDelMensaje() {
        Date date = new Date(getCreatedTimestampLong());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
