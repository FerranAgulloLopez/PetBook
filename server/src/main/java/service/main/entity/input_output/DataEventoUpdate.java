package service.main.entity.input_output;

import org.springframework.data.mongodb.core.mapping.Document;
import service.main.entity.User;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "domain")
public class DataEventoUpdate implements Serializable {
//
    //private String userEmail;
    private Integer any;
    private Integer mes;
    private Integer dia;
    private Integer hora;
    private Integer coordenadas;
    private Integer radio;


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
    public Integer getAny() {
        return any;
    }

    public void setAny(Integer any) {
        this.any = any;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Integer coordenadas) {
        this.coordenadas = coordenadas;
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
