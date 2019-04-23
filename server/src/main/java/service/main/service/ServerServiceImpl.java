package service.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.main.entity.*;
import service.main.entity.input_output.*;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;
import service.main.exception.NotFoundException;
import service.main.repositories.EventoRepository;
import service.main.repositories.MascotaRepository;
import service.main.repositories.UserRepository;
import service.main.util.SendEmailTLS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

    private static final String USERNOTDB = "The user does not exist in the database";
    private static final String EVENTNOTDB = "The event does not exist in the database";
    private static final String PETNOTDB = "The pet does not exist in the database";

    private SendEmailTLS mailsender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    /*
    User operations
     */

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        boolean result = user.get().checkPassword(password);
        return new OutLogin(result,user.get().isMailconfirmed());
    }

    public void RegisterUser(User input) throws BadRequestException {
        Optional<User> userToCheck = userRepository.findById(input.getEmail());
        if (userToCheck.isPresent()) throw new BadRequestException("The user already exists");
        userRepository.save(input);
    }

    public void ConfirmEmail(String email) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        user.get().setMailconfirmed(true);
        userRepository.save(user.get());
    }

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException, InternalErrorException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        if (user.get().isMailconfirmed()) throw new BadRequestException("The user has already verified his email");
        sendEmail(email);
    }

    private void sendEmail(String mail) throws InternalErrorException {
        if (mailsender == null) mailsender = new SendEmailTLS();
        String subject = "Petbook confirmation email";
        String text = "Hello. You've received this email because your email address was used for registering into Petbook application. " +
                "Please follow this link to confirm email address http://10.4.41.146:9999/mailconfirmation/" + mail + ". If you click the " +
                "link and it appears to be broken, please copy and paste it " +
                "into a new browser window. If you aren't able to access the link, please contact us via petbookasesores@gmail.com.";
        mailsender.sendEmail(mail,subject,text);
    }

    public User getUserByEmail(String email) throws NotFoundException {
        Optional<User> userToReturn = userRepository.findById(email);
        if (!userToReturn.isPresent()) throw new NotFoundException(USERNOTDB);
        else return userToReturn.get();
    }


    public void updateUserByEmail(String email, OutUpdateUserProfile userUpdated) throws NotFoundException {
        Optional<User> userToUpdate = userRepository.findById(email);
        if (!userToUpdate.isPresent()) throw new NotFoundException(USERNOTDB);
        else {
            User user = userToUpdate.get();
            user.setFirstName(userUpdated.getFirstName());
            user.setSecondName(userUpdated.getSecondName());
            user.setDateOfBirth(userUpdated.getDateOfBirth());
            user.setPostalCode(userUpdated.getPostalCode());
            userRepository.save(user);
        }
    }

    /*
    Event operations
     */

    public void creaEvento(DataEvento inputEvent) throws BadRequestException, NotFoundException {

        String usermail = inputEvent.getUserEmail();
        Optional<User> user = userRepository.findById(usermail);
        if(!user.isPresent()) throw new NotFoundException(USERNOTDB);

        Localizacion localizacion = new Localizacion(inputEvent.getCoordenadas(),inputEvent.getRadio());
        Evento event = new Evento(user.get(),localizacion.getId(),inputEvent.getFecha(),inputEvent.getTitulo(),inputEvent.getDescripcion(),inputEvent.isPublico());
        if(eventoRepository.existsById(event.getId())) throw new BadRequestException("The event already exists in the database");
        eventoRepository.save(event);
    }

    public List<Evento> findAllEventos() {
        return eventoRepository.findAll();
    }

    public List<Evento> findEventsByCreator(String creatormail) throws NotFoundException {
        if(!userRepository.existsById(creatormail)) throw new NotFoundException(USERNOTDB);
        return eventoRepository.findByemailCreador(creatormail);
    }

    public List<Evento> findEventsByParticipant(String participantmail) throws NotFoundException {
        if(!userRepository.existsById(participantmail)) throw new NotFoundException(USERNOTDB);
        return eventoRepository.findByParticipantesInOrderByFecha(participantmail);
    }


    public void updateEvento(String email, DataEventoUpdate evento) throws NotFoundException {
        Localizacion localizacion = new Localizacion(evento.getCoordenadas(),evento.getRadio());

        Evento evento2 = new Evento(email, localizacion.getId(), evento.getFecha(), evento.getDescripcion(), evento.getPublico(), evento.getParticipantes());
        if(!eventoRepository.existsById(evento2.getId())) throw new NotFoundException(EVENTNOTDB);
        eventoRepository.deleteById(evento2.getId());
        eventoRepository.insert(evento2);
    }

    public void addEventParticipant(String usermail, String creatormail, int coordinates, int radius, Date fecha) throws NotFoundException, BadRequestException {
        if(!userRepository.existsById(usermail)) throw new NotFoundException(USERNOTDB);
        Localizacion localizacion = new Localizacion(coordinates,radius);
        Evento event = new Evento(creatormail,localizacion.getId(),fecha);
        if(!eventoRepository.existsById(event.getId())) throw new NotFoundException(EVENTNOTDB);

        eventoRepository.addParticipant(usermail,event.getId());
    }


    public void deleteEvento(DataEvento event) throws NotFoundException {
        Localizacion localizacion = new Localizacion(event.getCoordenadas(),event.getRadio());

        Evento evento = new Evento(event.getUserEmail(), localizacion.getId(), event.getFecha());
        if(!eventoRepository.existsById(evento.getId())) throw new NotFoundException(EVENTNOTDB);
        eventoRepository.deleteById(evento.getId());
    }

    /*
    Pet operations
     */

    public void creaMascota(DataMascotaUpdate inMascota) throws BadRequestException, NotFoundException {
        Mascota mascota = new Mascota(inMascota.getNombre(),inMascota.getEmail(), inMascota.getEspecie(), inMascota.getRaza(), inMascota.getSexo(),
                                                            inMascota.getDescripcion(),inMascota.getEdad(),inMascota.getColor(),inMascota.getFoto());
        if(!userRepository.existsById(inMascota.getEmail())) throw new NotFoundException(USERNOTDB);
        if(mascotaRepository.existsById(mascota.getId())) throw new BadRequestException("The pet already exists in the database");
        mascotaRepository.save(mascota);
    }


    public Optional<Mascota> mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException {
        String id = nombreMascota+emailDuenyo;
        if(!userRepository.existsById(emailDuenyo)) throw new NotFoundException(USERNOTDB);
        if(!mascotaRepository.existsById(id)) throw new NotFoundException(PETNOTDB);
        return mascotaRepository.findById(id);
    }

    public void updateMascota(String email, DataMascotaUpdate mascota) throws NotFoundException {

        Mascota mascota2 = new Mascota(mascota.getNombre(), email, mascota.getEspecie(), mascota.getRaza(),
                                       mascota.getSexo(), mascota.getDescripcion(), mascota.getEdad(), mascota.getColor(), mascota.getFoto());

        String id = mascota2.getId();
        if(!mascotaRepository.existsById(id)) throw new NotFoundException(PETNOTDB);
        mascotaRepository.deleteById(id);
        mascotaRepository.save(mascota2);
    }

    public List<Mascota> findAllMascotasByUser(String email) throws NotFoundException{
        if(! userRepository.existsById(email)) throw new NotFoundException(USERNOTDB);

        List<Mascota> mascotas = mascotaRepository.findAll();
        List<Mascota> resultado = new ArrayList<>();

        for(Mascota mascota : mascotas)
            if(mascota.getUserEmail() != null && mascota.getUserEmail().equals(email)) resultado.add(mascota);

        return resultado;
    }

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException {
        String id = nombreMascota+emailDuenyo;
        if(! mascotaRepository.existsById(id)) throw new NotFoundException(PETNOTDB);
        mascotaRepository.deleteById(id);
    }

    public void removeDataBase() {
        userRepository.deleteAll();
        mascotaRepository.deleteAll();
        eventoRepository.deleteAll();
    }



}
