package service.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.main.entity.*;
import service.main.entity.output.OutLogin;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;
import service.main.repositories.EventoRepository;
import service.main.repositories.MascotaRepository;
import service.main.repositories.UserRepository;
import service.main.util.EmailServiceImpl;

import java.util.List;
import java.util.Optional;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        boolean result = user.get().checkPassword(password);
        return new OutLogin(result,user.get().isMailconfirmed());
    }

    public void RegisterUser(User input) {
        //TODO check if input has email and password
        userRepository.save(input);
    }
    public void ConfirmEmail(String email) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        user.get().setMailconfirmed(true);
        userRepository.save(user.get());
    }

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException("The user does not exist in the database.");
        if (user.get().isMailconfirmed()) throw new BadRequestException("The user has already verified his email.");
        sendEmail();
    }

    private void sendEmail() {
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.sendSimpleMessage("fewrna@gmail.com", "yea", "Just testing my friend");
    }


    public void creaEvento(String userEmail, Integer any, Integer mes, Integer dia, Integer hora, Integer coordenadas, Integer radio) {
        Fecha fecha2 = new Fecha(any, mes, dia ,hora);
        Localizacion localizacion = new Localizacion(coordenadas,radio);



        Evento evento = new Evento(userEmail, localizacion.getId(), fecha2.getId());
        eventoRepository.save(evento);
    }



    public List<Evento> findAllEventos() {
        return eventoRepository.findAll();
    }





    public void creaMascota(String email, String nom_mascota) {
        Mascota mascota = new Mascota(nom_mascota,email);
        mascotaRepository.save(mascota);
    }


    public Optional<Mascota> mascota_findById(String emailDuenyo, String nombreMascota) {
        String id = nombreMascota+emailDuenyo;
        return mascotaRepository.findById(id);
    }

    public List<Mascota> findAllMascotas() {
        return mascotaRepository.findAll();
    }





    public void deleteMascota(String emailDuenyo, String nombreMascota) {
        String id = nombreMascota+emailDuenyo;
        mascotaRepository.deleteById(id);
    }


}
