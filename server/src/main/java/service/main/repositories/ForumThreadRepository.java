package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.ForumThread;

import java.util.List;


public interface ForumThreadRepository extends MongoRepository<ForumThread, String> {

    public List<ForumThread> findAllByOrderByTopicAscCreationDateDesc();

}
