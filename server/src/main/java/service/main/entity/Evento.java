package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "domain")
public class Evento implements Serializable {

    @Id
    private String id;

    private String emailCreador;
    private String localizacion;
    private String fecha;

    private String descripcion;
    private Boolean publico;
    private Integer numero_asistentes;
    private ArrayList<User> participantes;

    public Evento() {}




    public Evento(  String creador,
                    String localizacion,
                    String fecha)
    {
        this.emailCreador = creador;
        this.localizacion = localizacion;
        this.fecha = fecha;

        this.setCreador(creador);
        this.setLocalizacion(localizacion);
        this.setFecha(fecha);
    }

    public Evento(  String creador,
                    String localizacion,
                    String fecha,
                    String descripcion,
                    Boolean publico,
                    Integer numero_asistentes,
                    ArrayList<User> participantes)
    {
        this.emailCreador = creador;
        this.localizacion = localizacion;
        this.fecha = fecha;

        this.setCreador(creador);
        this.setLocalizacion(localizacion);
        this.setFecha(fecha);
        this.setDescripcion(descripcion);
        this.setPublico(publico);
        this.setNumero_asistentes(numero_asistentes);
        this.setParticipantes(participantes);
    }


    private void makeId() {
        id = emailCreador+ " " + localizacion + fecha;
    }


    public String getId()                       { return id; }
    public String getCreador()                    { return emailCreador; }
    public String getLocalizacion()             { return localizacion; }
    public String getFecha()                     { return fecha; }
    public String getDescripcion()              { return descripcion; }
    public Boolean getPublico()                 { return publico; }
    public Integer getNumero_asistentes()       { return numero_asistentes; }
    public ArrayList<User> getParticipantes()   { return participantes; }


    public void setCreador(String creador)                            { this.emailCreador = creador; makeId(); }
    public void setLocalizacion(String localizacion)                { this.localizacion = localizacion; makeId(); }
    public void setFecha(String fecha)                               { this.fecha = fecha; makeId(); }
    public void setDescripcion(String descripcion)                  { this.descripcion = descripcion; }
    public void setPublico(Boolean publico)                         { this.publico = publico; }
    public void setNumero_asistentes(Integer numero_asistentes)     { this.numero_asistentes = numero_asistentes; }
    public void setParticipantes(ArrayList<User> participantes)     { this.participantes =  participantes; }





}
