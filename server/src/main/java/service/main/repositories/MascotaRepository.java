package service.main.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Mascota;


@Document(collection = "domain")
public interface MascotaRepository extends MongoRepository<Mascota, String> {


}
