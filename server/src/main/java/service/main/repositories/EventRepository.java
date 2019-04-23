package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Event;

import java.util.List;


public interface EventRepository extends MongoRepository<Event, String>, CustomizedEventRepository {

    public List<Event> findByemailCreador(String creatormail);

    public List<Event> findByParticipantesInOrderByFecha(String participantmail);

}
