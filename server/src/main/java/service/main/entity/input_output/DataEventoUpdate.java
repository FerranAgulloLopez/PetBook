package service.main.entity.input_output;

import org.springframework.data.mongodb.core.mapping.Document;
import service.main.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "domain")
public class DataEventoUpdate implements Serializable {

    //private String userEmail;
    private Integer coordenadas;
    private Integer radio;
    private Date fecha;

    private String descripcion;
    private Boolean publico;
    private List<String> participantes;

    public Integer getCoordenadas() {
        return coordenadas;
    }

    public Date getFecha() {
        return fecha;
    }

    public Integer getRadio() {
        return radio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean getPublico() {
        return publico;
    }

    public List<String> getParticipantes() {
        return participantes;
    }
}
