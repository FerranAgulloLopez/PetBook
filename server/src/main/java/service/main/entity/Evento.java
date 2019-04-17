package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "events")
public class Evento implements Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;

    private String emailCreador;
    private String localizacion;
    private Date fecha;

    private String titulo;
    private String descripcion;
    private boolean publico;
    private List<String> participantes;

    public Evento() {
        this.participantes = new ArrayList<>();
    }




    public Evento(  String creador,
                    String localizacion,
                    Date fecha)
    {
        this.emailCreador = creador;
        this.localizacion = localizacion;
        this.fecha = fecha;

        this.setCreador(creador);
        this.setLocalizacion(localizacion);
        this.setFecha(fecha);
    }

    public Evento(User creador,
                  String localizacion,
                  Date fecha,
                  String titulo,
                  String descripcion,
                  boolean publico)
    {
        this.emailCreador = creador.getEmail();
        this.localizacion = localizacion;
        this.fecha = fecha;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.publico = publico;
        this.participantes = new ArrayList<>();
        participantes.add(creador.getEmail());

        makeId();
    }

    public Evento(  String creador,
                    String localizacion,
                    Date fecha,
                    String descripcion,
                    boolean publico,
                    List<String> participantes)
    {
        this.emailCreador = creador;
        this.localizacion = localizacion;
        this.fecha = fecha;

        this.setCreador(creador);
        this.setLocalizacion(localizacion);
        this.setFecha(fecha);
        this.setDescripcion(descripcion);
        this.setPublico(publico);
        this.setParticipantes(participantes);
    }


    private void makeId() {
        id = emailCreador + " " + localizacion + fecha.toString();
    }


    public String getId()                       { return id; }
    public String getCreador()                    { return emailCreador; }
    public String getLocalizacion()             { return localizacion; }
    public Date getFecha()                     { return fecha; }
    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion()              { return descripcion; }
    public Boolean getPublico()                 { return publico; }
    public List<String> getParticipantes()   { return participantes; }


    public void setCreador(String creador)                            { this.emailCreador = creador; makeId(); }
    public void setLocalizacion(String localizacion)                { this.localizacion = localizacion; makeId(); }
    public void setFecha(Date fecha)                               { this.fecha = fecha; makeId(); }
    public void setDescripcion(String descripcion)                  { this.descripcion = descripcion; }
    public void setPublico(Boolean publico)                         { this.publico = publico; }
    public void setParticipantes(List<String> participantes)     { this.participantes =  participantes; }





}
