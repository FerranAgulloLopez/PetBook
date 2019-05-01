package service.main.entity.input_output.pet;

//
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "domain")
public class DataPet {

    private String email;
    private String nombre;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
