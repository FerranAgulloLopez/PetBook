package service.main.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import service.main.entity.Event;

import java.util.Date;
import java.util.List;


public interface EventRepository extends MongoRepository<Event, Long> {

    public List<Event> findByCreatorMail(String creatorMail);

    public List<Event> findByParticipantsInOrderByDate(String participantMail);

    public boolean existsByCreatorMailAndDateAndLocalization_AddressAndLocalization_LongitudeAndLocalization_Latitude(String creatorMail, Date date, String address, double latitude, double longitude);

}
