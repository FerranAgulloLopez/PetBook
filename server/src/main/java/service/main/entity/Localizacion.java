package service.main.entity;


// Stub
public class Localizacion {

    private int coordenadas;
    private int radio;



    public Localizacion() { // Fake
        coordenadas = 0;
        radio = 0;
    }

    public int getCoordenadas() { return coordenadas; }
    public int getRadio() { return radio; }


    public void setCoordenadas(int coordenadas) { this.coordenadas = coordenadas; }
    public void setRadio(int radio) { this.radio = radio; }
}
