package service.main.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Evento;

import java.util.Calendar;



public interface EventoRepository extends MongoRepository<Evento, String> {

}
