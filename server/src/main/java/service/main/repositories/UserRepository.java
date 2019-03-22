package service.main.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.User;


@Document(collection = "domain")
public interface UserRepository extends MongoRepository<User, String> {



}
