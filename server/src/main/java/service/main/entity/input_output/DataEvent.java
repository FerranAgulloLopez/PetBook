package service.main.entity.input_output;

import java.io.Serializable;
import java.util.Date;

public class DataEvent implements Serializable {

    private String userEmail;
    private Date fecha;
    private int coordenadas;
    private int radio;
    private String titulo;
    private String descripcion;
    private boolean publico;

    public String getUserEmail() {
        return userEmail;
    }

    public Date getFecha() {
        return fecha;
    }

    public Integer getCoordenadas() {
        return coordenadas;
    }

    public Integer getRadio() {
        return radio;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isPublico() {
        return publico;
    }
}
