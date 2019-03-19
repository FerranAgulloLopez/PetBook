package testing.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import testing.service.entity.EventoPublico;


@Document(collection = "domain")
public interface EventoPublicoRepository extends MongoRepository<EventoPublico, String> {


}
