package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.InterestSite;

import java.util.List;

public interface InterestSiteRepository extends MongoRepository<InterestSite, Long> {

    public List<InterestSite> findByAccepted (boolean accepted);

    public boolean existsByName (String name);


}
