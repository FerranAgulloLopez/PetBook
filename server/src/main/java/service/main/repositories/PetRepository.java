package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Pet;

public interface PetRepository extends MongoRepository<Pet, String> {


}
