package service.main.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "interestsites")
public class InterestSite implements Serializable {

    @Id
    private String id;

    private String nombre;
    private String localizacion;

    private String descripcion;
    private String tipo;
    private int numeroVotos;
    private boolean aceptado;
    private String emailCreador;



    public InterestSite() {
        nombre = "NULL";
        localizacion = "NULL";

        makeId();
    }

    public InterestSite(String nombre,
                        String localizacion)
    {

        this.nombre = "NULL";
        this.localizacion = "NULL";


        setNombre(nombre);
        setLocalizacion(localizacion);
    }


    public InterestSite(String nombre,
                        String localizacion,
                        String descripcion,
                        String tipo,
                        int numeroVotos,
                        boolean aceptado,
                        String emailCreador)
    {
        this.nombre = "NULL";
        this.localizacion = "NULL";



        setNombre(nombre);
        setLocalizacion(localizacion);
        setDescripcion(descripcion);
        setTipo(tipo);
        setNumeroVotos(numeroVotos);
        setAceptado(aceptado);
        setEmailCreador(emailCreador);
    }


    private void makeId() {
        id = nombre + " " + localizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        makeId();
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
        makeId();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNumeroVotos() {
        return numeroVotos;
    }

    public void setNumeroVotos(int numeroVotos) {
        this.numeroVotos = numeroVotos;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public String getEmailCreador() {
        return emailCreador;
    }

    public void setEmailCreador(String emailCreador) {
        this.emailCreador = emailCreador;
    }
}
