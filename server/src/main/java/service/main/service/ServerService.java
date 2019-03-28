package service.main.service;

import service.main.entity.Evento;
import service.main.entity.Mascota;
import service.main.entity.User;
import service.main.entity.output.OutLogin;
import service.main.entity.output.OutUpdateUserProfile;
import service.main.exception.AlreadyExistsException;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

public interface ServerService {
//
    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException;

    public void RegisterUser(User input);

    public User getUserByEmail(String email) throws NotFoundException;

    public void updateUserByEmail(String email, OutUpdateUserProfile user) throws NotFoundException;

    public void creaEvento(String userEmail, Integer any, Integer mes, Integer dia, Integer hora, Integer coordenadas, Integer radio) throws AlreadyExistsException, NotFoundException;
    public List<Evento> findAllEventos();


    public void creaMascota(String email, String nom_mascota) throws AlreadyExistsException, NotFoundException;
    Optional<Mascota> mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException;

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException;

    public List<Mascota> findAllMascotasByUser(String email) throws NotFoundException;

    public void removeDataBase();



}
