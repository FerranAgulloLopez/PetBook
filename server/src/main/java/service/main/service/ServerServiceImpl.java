package service.main.service;

import org.mockito.internal.matchers.Not;
import org.omg.CORBA.NO_IMPLEMENT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.main.entity.*;
import service.main.entity.input_output.*;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;
import service.main.exception.NotFoundException;
import service.main.repositories.*;
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
    private static final String SITENOTDB = "The interest site does not exist in the database";
    private static final String NOTPICTURE = "The user does not have profile picture in the database";
    private static final String USER_NOT_IN_EVENT = "The user does not participate in the event";
    private static final String THREADNOTDB = "The forum thread does not exist in the database";


    private SendEmailTLS mailsender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventoRepository;

    @Autowired
    private PetRepository mascotaRepository;

    @Autowired
    private InterestSiteRepository interestSiteRepository;

    @Autowired
    private ForumThreadRepository forumThreadRepository;



    /*
    User operations
     */

    public OutLogin ConfirmLogin(String email, String password) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        boolean result = user.get().checkPassword(password);
        return new OutLogin(result,user.get().isMailconfirmed());
    }

    public void RegisterUser(DataUser inputUser) throws BadRequestException {
        Optional<User> userToCheck = userRepository.findById(inputUser.getEmail());
        if (userToCheck.isPresent()) throw new BadRequestException("The user already exists");
        userRepository.save(inputUser.toUser());
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

    @Override
    public DataImage getProfilePicture(String email) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        else {
            String Picture = user.get().getFoto();
            if (Picture == null) throw new NotFoundException(NOTPICTURE);
            DataImage image = new DataImage();
            image.setImage(Picture);
            return image;
        }
    }

    @Override
    public void setProfilePicture(String email, String picture) throws NotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        else {
            User userToSave = user.get();
            userToSave.setFoto(picture);
            userRepository.save(userToSave);
        }
    }



    /*
    Event operations
     */

    public void creaEvento(DataEventUpdate inputEvent) throws BadRequestException, NotFoundException {

        String usermail = inputEvent.getUserEmail();
        Optional<User> user = userRepository.findById(usermail);
        if(!user.isPresent()) throw new NotFoundException(USERNOTDB);

        Localization localizacion = new Localization(inputEvent.getCoordenadas(),inputEvent.getRadio());
        Event event = new Event(user.get(),localizacion.getId(),inputEvent.getFecha(),inputEvent.getTitulo(),inputEvent.getDescripcion(),inputEvent.isPublico());
        if(eventoRepository.existsById(event.getId())) throw new BadRequestException("The event already exists in the database");
        eventoRepository.save(event);
    }

    public List<Event> findAllEventos() {
        return eventoRepository.findAll();
    }

    public List<Event> findEventsByCreator(String creatormail) throws NotFoundException {
        if(!userRepository.existsById(creatormail)) throw new NotFoundException(USERNOTDB);
        return eventoRepository.findByemailCreador(creatormail);
    }

    public List<Event> findEventsByParticipant(String participantmail) throws NotFoundException {
        if(!userRepository.existsById(participantmail)) throw new NotFoundException(USERNOTDB);
        return eventoRepository.findByParticipantesInOrderByFecha(participantmail);
    }

    public void updateEvento(DataEventUpdate evento) throws NotFoundException {
        Localization localizacion = new Localization(evento.getCoordenadas(),evento.getRadio());

        Event evento2 = new Event(evento.getUserEmail(), localizacion.getId(), evento.getFecha(), evento.getTitulo(), evento.getDescripcion(), evento.isPublico());
        Optional<Event> optEvent = eventoRepository.findById(evento2.getId());
        if(! optEvent.isPresent()) throw new NotFoundException(EVENTNOTDB);

        Event event3 = optEvent.get();
        event3.setDescripcion(evento2.getDescripcion());
        event3.setPublico(evento2.getPublico());
        event3.setTitulo(evento2.getTitulo());
        eventoRepository.save(event3);
    }

    public void addEventParticipant(String usermail, String creatormail, int coordinates, int radius, Date fecha) throws NotFoundException, BadRequestException {
        if(!userRepository.existsById(usermail)) throw new NotFoundException(USERNOTDB);
        Localization localizacion = new Localization(coordinates,radius);
        Event event = new Event(creatormail,localizacion.getId(),fecha);
        if(!eventoRepository.existsById(event.getId())) throw new NotFoundException(EVENTNOTDB);

        eventoRepository.addParticipant(usermail,event.getId());
    }

    public void removeEventParticipant(String usermail, String creatormail, int coordinates, int radius, Date fecha) throws NotFoundException, BadRequestException {
        if(!userRepository.existsById(usermail)) throw new NotFoundException(USERNOTDB);
        Localization localizacion = new Localization(coordinates,radius);
        Event evento = new Event(creatormail,localizacion.getId(),fecha);
        Optional<Event> optEvent =  eventoRepository.findById(evento.getId());
        if(! optEvent.isPresent()) throw new NotFoundException(EVENTNOTDB);
        Event event = optEvent.get();
        if(! event.userParticipates(usermail)) throw new BadRequestException(USER_NOT_IN_EVENT);
        event.removeUser(usermail);

        eventoRepository.save(event);
    }

    public void deleteEvento(DataEvent event) throws NotFoundException {
        Localization localizacion = new Localization(event.getCoordenadas(),event.getRadio());

        Event evento = new Event(event.getUserEmail(), localizacion.getId(), event.getFecha());
        if(!eventoRepository.existsById(evento.getId())) throw new NotFoundException(EVENTNOTDB);
        eventoRepository.deleteById(evento.getId());
    }



    /*
    Pet operations
     */

    public void creaMascota(DataPetUpdate inMascota) throws BadRequestException, NotFoundException {
        Pet mascota = new Pet(inMascota.getNombre(),inMascota.getEmail(), inMascota.getEspecie(), inMascota.getRaza(), inMascota.getSexo(),
                                                            inMascota.getDescripcion(),inMascota.getEdad(),inMascota.getColor(),inMascota.getFoto());
        if(!userRepository.existsById(inMascota.getEmail())) throw new NotFoundException(USERNOTDB);
        if(mascotaRepository.existsById(mascota.getId())) throw new BadRequestException("The pet already exists in the database");
        mascotaRepository.save(mascota);
    }

    public Pet mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException {
        String id = nombreMascota+emailDuenyo;
        if(!userRepository.existsById(emailDuenyo)) throw new NotFoundException(USERNOTDB);
        Optional<Pet> pet = mascotaRepository.findById(id);
        if(!pet.isPresent()) throw new NotFoundException(PETNOTDB);
        return pet.get();
    }

    public void updateMascota(String email, String name, DataPetUpdate inMascota) throws NotFoundException {
        Pet mascota = new Pet(name, email);
        String id = mascota.getId();
        Optional<Pet> optPet = mascotaRepository.findById(id);
        if(! optPet.isPresent()) throw new NotFoundException(PETNOTDB);
        Pet pet = optPet.get();

        if(! name.equals(inMascota.getNombre() ) ) mascotaRepository.deleteById(id); // nombre cambia -->  id cambia.    Asi que hay que destruir la instancia para que no quede mascotas fantasmas

        pet.setColor(inMascota.getColor());
        pet.setDescripcion(inMascota.getDescripcion());
        pet.setEdad(inMascota.getEdad());
        pet.setEspecie(inMascota.getEspecie());
        pet.setFoto(inMascota.getFoto());
        pet.setRaza(inMascota.getRaza());
        pet.setSexo(inMascota.getSexo());
        pet.setNombre(inMascota.getNombre());

        mascotaRepository.save(pet);
    }

    public List<Pet> findAllMascotasByUser(String email) throws NotFoundException{
        if(!userRepository.existsById(email)) throw new NotFoundException(USERNOTDB);

        List<Pet> mascotas = mascotaRepository.findAll();
        List<Pet> resultado = new ArrayList<>();

        for(Pet mascota : mascotas)
            if(mascota.getUserEmail() != null && mascota.getUserEmail().equals(email)) resultado.add(mascota);

        return resultado;
    }

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException {
        String id = nombreMascota+emailDuenyo;
        if(!mascotaRepository.existsById(id)) throw new NotFoundException(PETNOTDB);
        mascotaRepository.deleteById(id);
    }



    /*
    Interest Site operations
     */

    public void createInterestSite(DataInterestSite inputInterestSite) throws BadRequestException, NotFoundException {
        InterestSite interestSite = inputInterestSite.toInterestSite();
        if (!userRepository.existsById(inputInterestSite.getCreatorMail())) throw new NotFoundException(USERNOTDB);
        if (interestSiteRepository.existsById(interestSite.getId())) throw new BadRequestException("The interest site already exists in the database");
        interestSiteRepository.save(interestSite);
    }

    public InterestSite getInterestSite(String name, String localization) throws NotFoundException {
        InterestSite aux = new InterestSite(name,localization);
        Optional<InterestSite> interestSite = interestSiteRepository.findById(aux.getId());
        if (!interestSite.isPresent()) throw new NotFoundException(SITENOTDB);
        return interestSite.get();
    }

    public void voteInterestSite(String interestSiteName, String interestSiteLocalization, String userEmail) throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userEmail)) throw new NotFoundException(USERNOTDB);
        InterestSite aux = new InterestSite(interestSiteName,interestSiteLocalization);
        Optional<InterestSite> interestSite_opt = interestSiteRepository.findById(aux.getId());
        if (!interestSite_opt.isPresent()) throw new NotFoundException(SITENOTDB);
        InterestSite interestSite = interestSite_opt.get();
        boolean found = false;
        for (int i = 0; !found && i < interestSite.getVotes().size(); ++i) {
            String email = interestSite.getVotes().get(i);
            if (email.equals(userEmail)) found = true;
        }
        if (found) throw new BadRequestException("The user already voted this interest site");
        interestSite.addVote(userEmail);
        interestSiteRepository.save(interestSite);
    }



    /*
    Forum operations
     */

    @Override
    public List<ForumThread> getAllForumThreads() {
        return forumThreadRepository.findAll();
    }

    @Override
    public ForumThread getForumThread(String creatorMail, String title) throws NotFoundException {
        ForumThread aux = new ForumThread(creatorMail,title);
        Optional<ForumThread> forumThread_opt = forumThreadRepository.findById(aux.getId());
        if (!forumThread_opt.isPresent()) throw new NotFoundException(THREADNOTDB);
        return forumThread_opt.get();
    }


    /*public void createNewForumTopic(String topicName) throws BadRequestException {
        if (ForumThread.getTopics().contains(topicName)) throw new BadRequestException("The topic already exists in the database");
        ForumThread.addTopic(topicName);
    }*/

    @Override
    public void createNewForumThread(DataForumThread dataForumThread) throws BadRequestException, NotFoundException {
        if (!userRepository.existsById(dataForumThread.getCreatorMail())) throw new NotFoundException(USERNOTDB);
        ForumThread forumThread = dataForumThread.toForum();
        if (forumThreadRepository.existsById(forumThread.getId())) throw new BadRequestException("The forum thread already exists");
        forumThreadRepository.save(forumThread);
    }

    @Override
    public void deleteForumThread(String creatorMail, String title) throws NotFoundException {
        ForumThread aux = new ForumThread(creatorMail,title);
        if (!forumThreadRepository.existsById(aux.getId())) throw new NotFoundException(THREADNOTDB);
        forumThreadRepository.deleteById(aux.getId());
    }

    @Override
    public void addForumComment(String creatorMail, String title, DataForumComment dataForumComment) throws NotFoundException {
        ForumThread aux = new ForumThread(creatorMail,title);
        Optional<ForumThread> forumThread_opt = forumThreadRepository.findById(aux.getId());
        if (!forumThread_opt.isPresent()) throw new NotFoundException(THREADNOTDB);
        ForumThread forumThread = forumThread_opt.get();
        forumThread.addComment(dataForumComment.toComment());
        forumThreadRepository.save(forumThread);
    }




    /*
    Test operations TODO remove this section in the future
     */

    public void removeDataBase() {
        userRepository.deleteAll();
        mascotaRepository.deleteAll();
        eventoRepository.deleteAll();
        interestSiteRepository.deleteAll();
        forumThreadRepository.deleteAll();
    }



}
