package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Ban;

import java.util.List;
import java.util.Optional;

public interface BanRepository extends MongoRepository<Ban, Long> {

    public List<Ban> findByClosed(boolean closed);

    public Optional<Ban> findByCreatorMailAndSuspectMail(String creatorMail, String suspectMail);
}
