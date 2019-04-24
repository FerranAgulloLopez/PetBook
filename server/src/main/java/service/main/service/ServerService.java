package service.main.service;

import service.main.entity.Event;
import service.main.entity.InterestSite;
import service.main.entity.Pet;
import service.main.entity.User;
import service.main.entity.input_output.*;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;
import service.main.exception.NotFoundException;

import java.util.Date;
import java.util.List;

public interface ServerService {

    /*
    User operations
     */

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException, InternalErrorException;

    public void RegisterUser(DataUser inputUser) throws BadRequestException;

    public User getUserByEmail(String email) throws NotFoundException;

    public void updateUserByEmail(String email, OutUpdateUserProfile user) throws NotFoundException;


    /*
    Event operations
     */

    public void creaEvento(DataEvent event) throws BadRequestException, NotFoundException;

    public List<Event> findAllEventos();

    public List<Event> findEventsByCreator(String creatormail) throws NotFoundException;

    public List<Event> findEventsByParticipant(String participantmail) throws NotFoundException;

    public void updateEvento(String email, DataEventUpdate evento) throws NotFoundException;

    public void addEventParticipant(String usermail, String creatormail, int coordinates, int radius, Date fecha) throws NotFoundException, BadRequestException;

    public void deleteEvento(DataEvent event) throws NotFoundException;


    /*
    Pet operations
     */

    public void creaMascota(DataPetUpdate mascota) throws BadRequestException, NotFoundException;

    public Pet mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException;

    void updateMascota(String email, DataPetUpdate mascota) throws NotFoundException;

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException;

    public List<Pet> findAllMascotasByUser(String email) throws NotFoundException;

    public void removeDataBase();


    /*
    Interest site operations
     */

    public void createInterestSite(DataInterestSite inputInterestSite) throws BadRequestException, NotFoundException;

    public InterestSite getInterestSite(String name, String localization) throws NotFoundException;


    public String getProfilePicture(String email) throws NotFoundException;

    public void setProfilePicture(String email, String picture) throws NotFoundException;
}
