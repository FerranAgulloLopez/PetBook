package service.main.entity.input_output;

import org.springframework.data.mongodb.core.mapping.Document;
import service.main.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Document(collection = "domain")
public class DataEventoUpdate implements Serializable {
//
    //private String userEmail;
    private Integer coordenadas;
    private Integer radio;
    private Date fecha;

    private String descripcion;
    private Boolean publico;
    private Integer numero_asistentes;
    private ArrayList<User> participantes;


/*
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
*/

    public Integer getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Integer coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getRadio() {
        return radio;
    }

    public void setRadio(Integer radio) {
        this.radio = radio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }

    public Integer getNumero_asistentes() {
        return numero_asistentes;
    }

    public void setNumero_asistentes(Integer numero_asistentes) {
        this.numero_asistentes = numero_asistentes;
    }

    public ArrayList<User> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<User> participantes) {
        this.participantes = participantes;
    }
}
