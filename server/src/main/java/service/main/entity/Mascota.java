package service.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

//
@Document(collection = "domain")
public class Mascota implements Serializable {

    @Id
    private String id;

    private String nombre;
    private String userEmail;

    private String especie;
    private String raza;
    private String sexo;
    private String descripcion;
    private int edad;
    private String color;
    private String foto;    // Esta encodeado en Base64(byte[]) y luego pasado a String[].
                            // Para conseguir la foto, pasar a Base64(byte[]) y luego convertir a file.
                            // File   -> byte[] -> String[]
                            // String[] -> byte[] -> File




    public Mascota() {
        nombre = "NULL";
        userEmail = "NULL";

        makeId();
    }

    public Mascota( String nombre,
                    String userEmail)
    {

        this.userEmail = "";
        this.nombre = "";

        setNombre(nombre);
        setUserEmail(userEmail);
    }


    public Mascota( String nombre,
                    String userEmail,
                    String especie,
                    String raza,
                    String sexo,
                    String descripcion,
                    int edad,
                    String color,
                    String foto)
    {
        this.userEmail = "";
        this.nombre = "";


        setNombre(nombre);
        setUserEmail(userEmail);

        setEspecie( especie);
        setRaza(raza);
        setSexo(sexo);
        setDescripcion(descripcion);
        setColor(color);
        setEdad(edad);
        setFoto(foto);
    }


    private void makeId() {
        id = nombre + userEmail;
    }

        // GETTERS
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUserEmail() { return userEmail; }

    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public String getSexo() { return sexo; }
    public String getDescripcion() { return descripcion; }
    public String getFoto() { return foto; }
    public String getColor() {
        return color;
    }

    public int getEdad() {
        return edad;
    }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; makeId(); }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; makeId(); }

    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza) { this.raza = raza; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setColor(String color) {
        this.color = color;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
}
