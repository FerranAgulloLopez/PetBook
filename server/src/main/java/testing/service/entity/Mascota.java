package testing.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "domain")
public class Mascota implements Serializable {

    @Id
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private String descripcion;
    private String foto;    // Esto como lo hacemos?





    public Mascota( String nombre,
                    String especie,
                    String raza,
                    String sexo,
                    String descripcion,
                    String foto)
    {
        setNombre(nombre);
        setEspecie( especie);
        setRaza(raza);
        setSexo(sexo);
        setDescripcion(descripcion);
        setFoto(foto);
    }

    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public String getSexo() { return sexo; }
    public String getDescripcion() { return descripcion; }
    public String getFoto() { return foto; }


    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza) { this.raza = raza; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFoto(String foto) { this.foto = foto; }
}
