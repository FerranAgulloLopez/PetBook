package service.main.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Evento;

import java.util.Calendar;
import java.util.List;


public interface EventoRepository extends MongoRepository<Evento, String> {

    public List<Evento> findByemailCreador(String creatormail);

}
