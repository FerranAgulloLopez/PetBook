package service.main.entity.output;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "domain")
public class DataEvento implements Serializable {
//
    private String userEmail;
    private Integer any;
    private Integer mes;
    private Integer dia;
    private Integer hora;
    private Integer coordenadas;
    private Integer radio;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

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
}
