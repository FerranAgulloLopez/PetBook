package service.main.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Localizacion;


@Document(collection = "domain")
public interface LocalizacionRepository extends MongoRepository<Localizacion, String> {


}
