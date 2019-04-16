package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "domain")
public class Evento implements Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;

    private String emailCreador;
    private String localizacion;
    private Date fecha;

    private String descripcion;
    private boolean publico;
    private int numero_asistentes;
    private ArrayList<User> participantes;

    public Evento() {}




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

    public Evento(  String creador,
                    String localizacion,
                    Date fecha,
                    String descripcion,
                    boolean publico,
                    int numero_asistentes,
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
        id = emailCreador+ " " + localizacion + fecha.toString();
    }


    public String getId()                       { return id; }
    public String getCreador()                    { return emailCreador; }
    public String getLocalizacion()             { return localizacion; }
    public Date getFecha()                     { return fecha; }
    public String getDescripcion()              { return descripcion; }
    public Boolean getPublico()                 { return publico; }
    public Integer getNumero_asistentes()       { return numero_asistentes; }
    public ArrayList<User> getParticipantes()   { return participantes; }


    public void setCreador(String creador)                            { this.emailCreador = creador; makeId(); }
    public void setLocalizacion(String localizacion)                { this.localizacion = localizacion; makeId(); }
    public void setFecha(Date fecha)                               { this.fecha = fecha; makeId(); }
    public void setDescripcion(String descripcion)                  { this.descripcion = descripcion; }
    public void setPublico(Boolean publico)                         { this.publico = publico; }
    public void setNumero_asistentes(Integer numero_asistentes)     { this.numero_asistentes = numero_asistentes; }
    public void setParticipantes(ArrayList<User> participantes)     { this.participantes =  participantes; }





}
