package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.ForumThread;

public interface ForumThreadRepository extends MongoRepository<ForumThread, String> {

}
