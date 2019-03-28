package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
//
@Document(collection = "domain")
public class Localizacion implements Serializable {

    @Id
    private String id;

    private Integer coordenadas;
    private Integer radio;

    //String separador = "/"; // Separador entre valores

    public Localizacion() {
        this.coordenadas = 0;
        this.radio = 0;
        setRadio(0);
        setCoordenadas(0);
    }

    public Localizacion(Integer coordenadas, Integer radio) {
        this.coordenadas = 0;
        this.radio = 0;
        setRadio(radio);
        setCoordenadas(coordenadas);
    }


    private void makeId() {
        id = coordenadas.toString() +  radio.toString();
    }


    public String getId() { return id; }
    public int getCoordenadas() { return coordenadas; }
    public int getRadio() { return radio; }


    public void setRadio(Integer radio) {
        this.radio = radio;
        makeId();
    }
    public void setCoordenadas(Integer coordenadas) {
        this.coordenadas = coordenadas;
        makeId();
    }






}
