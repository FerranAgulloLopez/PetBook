package service.main.repositories;

import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;

public interface CustomizedEventoRepository {

    public void addParticipant(String participantmail, String eventId) throws BadRequestException;
}
