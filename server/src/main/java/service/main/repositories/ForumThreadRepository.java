package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.ForumThread;

import java.util.List;
import java.util.Optional;


public interface ForumThreadRepository extends MongoRepository<ForumThread, String> {

    public List<ForumThread> findAllByOrderByTopicAscCreationDateDesc();

    public boolean existsByCommentsContaining(String id);

    public Optional<ForumThread> findOptionalByComments_Id(String id);

    public Optional<ForumThread> findOptionalByCommentsContaining(String id);


}
