package service.main.service;

import service.main.entity.Evento;
import service.main.entity.Mascota;
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

    public void creaEvento(DataEvento event) throws BadRequestException, NotFoundException;

    public List<Evento> findAllEventos();

    public List<Evento> findEventsByCreator(String creatormail) throws NotFoundException;

    public List<Evento> findEventsByParticipant(String participantmail) throws NotFoundException;

    public void updateEvento(String email, DataEventoUpdate evento) throws NotFoundException;

    public void addEventParticipant(String usermail, String creatormail, int coordinates, int radius, Date fecha) throws NotFoundException, BadRequestException;

    public void deleteEvento(DataEvento event) throws NotFoundException;


    /*
    Pet operations
     */

    public void creaMascota(DataMascotaUpdate mascota) throws BadRequestException, NotFoundException;

    public Mascota mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException;

    void updateMascota(String email, DataMascotaUpdate mascota) throws NotFoundException;

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException;

    public List<Mascota> findAllMascotasByUser(String email) throws NotFoundException;

    public void removeDataBase();


}
