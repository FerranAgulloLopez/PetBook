package service.main.service;

import service.main.entity.Evento;
import service.main.entity.Mascota;
import service.main.entity.User;
import service.main.entity.output.OutLogin;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;
import java.util.List;
import java.util.Optional;

public interface ServerService {

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException;

    public void RegisterUser(User input);

    public void creaEvento(String userEmail, Integer any, Integer mes, Integer dia, Integer hora, Integer coordenadas, Integer radio);
    public List<Evento> findAllEventos();


    public void creaMascota(String email, String nom_mascota);
    Optional<Mascota> mascota_findById(String emailDuenyo, String nombreMascota);

    public void deleteMascota(String emailDuenyo, String nombreMascota);

    public List<Mascota> findAllMascotas();
}
