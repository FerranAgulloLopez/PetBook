package service.main.entity;

import java.io.Serializable;

public class Localization implements Serializable {

    private String id;

    private int coordenadas;
    private int radio;

    public Localization() {
        this.coordenadas = 0;
        this.radio = 0;
        makeId();
    }

    public Localization(int coordenadas, int radio) {
        this.coordenadas = coordenadas;
        this.radio = radio;
        makeId();
    }


    private void makeId() {
        id = "" + coordenadas + radio;
    }


    public String getId() { return id; }
    public int getCoordenadas() { return coordenadas; }
    public int getRadio() { return radio; }


    public void setRadio(int radio) {
        this.radio = radio;
        makeId();
    }
    public void setCoordenadas(int coordenadas) {
        this.coordenadas = coordenadas;
        makeId();
    }






}
