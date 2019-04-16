package service.main.entity;

import java.io.Serializable;

public class Localizacion implements Serializable {

    private String id;

    private Integer coordenadas;
    private Integer radio;

    public Localizacion() {
        this.coordenadas = 0;
        this.radio = 0;
        makeId();
    }

    public Localizacion(Integer coordenadas, Integer radio) {
        this.coordenadas = 0;
        this.radio = 0;
        makeId();
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
