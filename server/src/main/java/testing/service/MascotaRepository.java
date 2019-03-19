package testing.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import testing.service.entity.Mascota;


@Document(collection = "domain")
public interface MascotaRepository extends MongoRepository<Mascota, String> {


}
