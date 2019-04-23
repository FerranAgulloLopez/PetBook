package service.main.repositories;

import service.main.exception.BadRequestException;

public interface CustomizedEventRepository {

    public void addParticipant(String participantmail, String eventId) throws BadRequestException;
}
