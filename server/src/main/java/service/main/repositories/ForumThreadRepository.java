package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.ForumThread;

import java.util.List;


public interface ForumThreadRepository extends MongoRepository<ForumThread, Long> {

    public List<ForumThread> findAllByOrderByTopicAscCreationDateDesc();

    public boolean existsByCreatorMailAndTitle(String creatorMail, String title);

}
