package testing.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Document(collection = "domain")
public class EventoPublico extends Evento {


    public EventoPublico(User creador, Localizacion localizacion, Fecha fecha, String descripcion, ArrayList<User> participantes) {
        super(creador, localizacion, fecha, descripcion, participantes);
    }

}
