package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "domain")
public class Evento implements Serializable {

    @Id
    private User creador;

    private Localizacion localizacion;
    private Fecha fecha;

    private String descripcion;

    private ArrayList<User> participantes;



    public Evento(  User creador,
                    Localizacion localizacion,
                    Fecha fecha,
                    String descripcion, ArrayList<User> participantes)
    {
        this.setCreador(creador);
        this.setLocalizacion(localizacion);
        this.setFecha(fecha);
        this.setDescripcion(descripcion);
        this.setParticipantes(participantes);
    }

    public User getCreador() { return creador; }
    public Localizacion getLocalizacion() { return localizacion; }
    public Fecha getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public ArrayList<User> getParticipantes() { return participantes; }


    public void setCreador(User creador) { this.creador = creador; }
    public void setLocalizacion(Localizacion localizacion) { this.localizacion = localizacion; }
    public void setFecha(Fecha fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setParticipantes(ArrayList<User> participantes) { this.participantes =  participantes; }



}
