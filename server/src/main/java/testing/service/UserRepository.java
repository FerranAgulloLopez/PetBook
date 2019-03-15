package testing.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import testing.service.entity.User;


@Document(collection = "domain")
public interface UserRepository extends MongoRepository<User, String> {


}
