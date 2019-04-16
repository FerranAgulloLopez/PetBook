package service.main.entity.input_output;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "domain")
public class DataEvento implements Serializable {

    private String userEmail;
    private Date fecha;
    private Integer coordenadas;
    private Integer radio;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
