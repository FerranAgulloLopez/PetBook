package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Pet;

import java.util.List;

public interface PetRepository extends MongoRepository<Pet, String> {

    public List<Pet> findByUserEmail(String useremail);


}
