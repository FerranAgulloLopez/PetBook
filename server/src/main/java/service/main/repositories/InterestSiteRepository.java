package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.InterestSite;

public interface InterestSiteRepository extends MongoRepository<InterestSite, String> {


}
