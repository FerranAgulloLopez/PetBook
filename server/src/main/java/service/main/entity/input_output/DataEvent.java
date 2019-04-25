package service.main.entity.input_output;

import java.io.Serializable;
import java.util.Date;

public class DataEvent implements Serializable {

    private String userEmail;
    private Integer coordenadas;
    private Integer radio;
    private Date fecha;

    public String getUserEmail() { return userEmail; }

    public Integer getCoordenadas() {
        return coordenadas;
    }

    public Integer getRadio() { return radio; }

    public Date getFecha() {
        return fecha;
    }

}