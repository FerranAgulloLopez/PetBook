package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.User;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {

    public List<User> findByPostalCode(String postalcode);

}
