package service.main.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import service.main.config.JwtConfig;
import service.main.entity.*;
import service.main.entity.input_output.event.CustomEventCalendarIdAdapter;
import service.main.entity.input_output.event.DataEvent;
import service.main.entity.input_output.event.DataEventUpdate;
import service.main.entity.input_output.forum.DataForumComment;
import service.main.entity.input_output.forum.DataForumCommentUpdate;
import service.main.entity.input_output.forum.DataForumThread;
import service.main.entity.input_output.forum.DataForumThreadUpdate;
import service.main.entity.input_output.image.DataImage;
import service.main.entity.input_output.interestsite.DataInterestSite;
import service.main.entity.input_output.interestsite.DataInterestSiteUpdate;
import service.main.entity.input_output.pet.DataPetUpdate;
import service.main.entity.input_output.user.*;
import service.main.exception.BadRequestException;
import service.main.exception.ForbiddenException;
import service.main.exception.InternalServerErrorException;
import service.main.exception.NotFoundException;
import service.main.repositories.*;
import service.main.services.FireMessage;
import service.main.services.SequenceGeneratorService;
import service.main.util.DefaultImage;
import service.main.util.SendEmailTLS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("serverService")
public class ServerServiceImpl implements ServerService {

    private static final String USERNOTDB = "The user does not exist in the database";
    private static final String EVENTNOTDB = "The event does not exist in the database";
    private static final String PETNOTDB = "The pet does not exist in the database";
    private static final String SITENOTDB = "The interest site does not exist in the database";
    private static final String USER_NOT_IN_EVENT = "The user does not participate in the event";
    private static final String ALREADY_SENT_FRIEND_REQUEST = "The user already have sent a friend request to the other user";
    private static final String HAVENT_SENT_FRIEND_REQUEST = "The user havent sent a friend request to the other user";
    private static final String USERS_ALREADY_ARE_FRIENDS = "The users already are friends";
    private static final String ONE_OF_USERS_DONT_EXIST = "One of the users does not exist in the database";
    private static final String THREADNOTDB = "The forum thread does not exist in the database";
    private static final String USERS_ARE_NOT_FRIENDS = "The users are not friends";
    private static final String USER_HASNT_POSTAL_CODE = "The user has not a postal code";
    private static final String ARE_THE_SAME_USER = "Users are the same user";


    private SendEmailTLS mailsender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PetRepository mascotaRepository;

    @Autowired
    private InterestSiteRepository interestSiteRepository;

    @Autowired
    private ForumThreadRepository forumThreadRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;


    /*
    User operations
     */

    @Override
    public OutLogin ConfirmLogin(String email, String password, HttpServletResponse response) throws NotFoundException {
        User user = auxGetUser(email);
        boolean result = user.checkPassword(password);
        generateJWTToken(user, response);
        return new OutLogin(result,user.isMailconfirmed());
    }

    @Override
    public void RegisterUser(DataUser inputUser) throws BadRequestException {
        Optional<User> userToCheck = userRepository.findById(inputUser.getEmail());
        if (userToCheck.isPresent()) throw new BadRequestException("The user already exists");
        userRepository.save(inputUser.toUser());
    }

    @Override
    public void ConfirmEmail(String email) throws NotFoundException {
        User user = auxGetUser(email);
        user.setMailconfirmed(true);
        userRepository.save(user);
    }

    @Override
    public void SendConfirmationEmail(HttpServletRequest request) throws BadRequestException, InternalServerErrorException {
        String userMail = getLoggedUserMail();
        Optional<User> user = userRepository.findById(userMail);
        if (!user.isPresent()) throw new InternalServerErrorException("Error while loading user from the database");
        if (user.get().isMailconfirmed()) throw new BadRequestException("The user has already verified his email");
        JwtConfig config = new JwtConfig();
        String token = request.getHeader(config.getHeader());
        token = token.replaceAll(config.getPrefix(),"");
        sendEmail(userMail,token);
    }

    private void sendEmail(String mail, String token) throws InternalServerErrorException {
        if (mailsender == null) mailsender = new SendEmailTLS();
        String subject = "Petbook confirmation email";
        String text = "<p>Hello.</p><p> You've received this email because your email address was used for registering into Petbook application. " +
                "Please follow this <a href=\"http://10.4.41.146:9999/mailConfirmation/" + token + "\">link</a> to confirm your email address. If you click the " +
                "link and it appears to be broken, please copy and paste it " +
                "into a new browser window. If you aren't able to access the link, please contact us via petbookasesores@gmail.com.</p>";
        mailsender.sendEmail(mail,subject,text);
    }

    @Override
    public User getUserByEmail(String email) throws NotFoundException {
        return auxGetUser(email);
    }


    @Override
    public void updateUserByEmail(String email, OutUpdateUserProfile userUpdated) throws NotFoundException {
        User user = auxGetUser(email);
        user.setFirstName(userUpdated.getFirstName());
        user.setSecondName(userUpdated.getSecondName());
        user.setDateOfBirth(userUpdated.getDateOfBirth());
        user.setPostalCode(userUpdated.getPostalCode());
        userRepository.save(user);
    }

    @Override
    public DataImage getProfilePicture(String email) throws NotFoundException {
        User user = auxGetUser(email);
        String Picture = user.getFoto();
        if (Picture == null) Picture = DefaultImage.image;
        DataImage image = new DataImage();
        image.setImage(Picture);
        return image;
    }

    @Override
    public void setProfilePicture(String email, String picture) throws NotFoundException {
        User user = auxGetUser(email);
        user.setFoto(picture);
        userRepository.save(user);
    }

    @Override
    public void setTokenFirebase(String email, String token) throws NotFoundException {
        User user = auxGetUser(email);
        user.setTokenFirebase(token);
        System.out.println("TOKEN: " + user.getTokenFirebase());
        userRepository.save(user);
    }



    @Override
    public void updatePassword(String email, DataUpdatePassword dataUpdatePassword) throws NotFoundException, BadRequestException {
        User user = auxGetUser(email);
        boolean result = user.checkPassword(dataUpdatePassword.getOldPassword());
        if (!result) throw new BadRequestException("The oldPassword is not correct");
        user.setPassword(dataUpdatePassword.getNewPassword());
        userRepository.save(user);
    }



    /*
    WallPosts
     */

    @Override
    public List<DataWallPostAux> getUserWallPosts(String userMail) throws NotFoundException {
        User user = auxGetUser(userMail);
        List<DataWallPostAux> result = new ArrayList<>();
        for (WallPost wallPost: user.getWallPosts()) {
            DataWallPostAux aux = new DataWallPostAux(wallPost, user.getEmail(), user.getFoto());
            result.add(aux);
        }
        return result;
    }

    @Override
    public DataWallPostAux getUserWallPost(String userMail, long wallPostId) throws NotFoundException {
        User user = auxGetUser(userMail);
        WallPost wallPost = user.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        return new DataWallPostAux(wallPost, user.getEmail(), user.getFoto());
    }

    @Override
    public void createWallPost(DataWallPost dataWallPost) throws InternalServerErrorException, BadRequestException {
        String userMail = getLoggedUserMail();
        User user;
        try {
            user = auxGetUser(userMail);
        } catch (NotFoundException e) {
            throw new InternalServerErrorException("Error while loading user from the database");
        }
        if (!dataWallPost.inputCorrect()) throw new BadRequestException("The input data is not well formed");
        WallPost wallPost = new WallPost(dataWallPost.getDescription(),dataWallPost.getCreationDate(),null);
        wallPost.setId(sequenceGeneratorService.generateSequence(WallPost.SEQUENCE_NAME));
        user.addWallPost(wallPost);
        userRepository.save(user);
    }

    @Override
    public void updateWallPost(long wallPostId, DataWallPostUpdate dataWallPostUpdate) throws NotFoundException, InternalServerErrorException, BadRequestException {
        String userMail = getLoggedUserMail();
        User user;
        try {
            user = auxGetUser(userMail);
        } catch (NotFoundException e) {
            throw new InternalServerErrorException("Error while loading user from the database");
        }
        WallPost wallPost = user.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        if (!dataWallPostUpdate.inputCorrect()) throw new BadRequestException("The input data is not well formed");
        wallPost.setDescription(dataWallPostUpdate.getDescription());
        wallPost.setUpdateDate(dataWallPostUpdate.getUpdateDate());
        userRepository.save(user);
    }

    @Override
    public void likeWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException {
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not like his own posts");
        if (!wallPost.addLike(userMail)) throw new BadRequestException("A user can not like two times the same post");
        userRepository.save(creatorUser);
    }

    @Override
    public void unLikeWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException {
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not unlike his own posts");
        if (!wallPost.deleteLike(userMail)) throw new BadRequestException("The user has not a like in this post");
        userRepository.save(creatorUser);
    }

    @Override
    public void loveWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException {
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not love his own posts");
        if (!wallPost.addLove(userMail)) throw new BadRequestException("A user can not love two times the same post");
        userRepository.save(creatorUser);
    }

    @Override
    public void unloveWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException {
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not unlove his own posts");
        if (!wallPost.deleteLove(userMail)) throw new BadRequestException("The user has not a love in this post");
        userRepository.save(creatorUser);
    }

    @Override
    public void retweetWallPost(String creatorMail, long wallPostId, DataWallPost dataRetweet) throws NotFoundException, BadRequestException, InternalServerErrorException {
        if (!dataRetweet.inputCorrect()) throw new BadRequestException("The input is not well formed");
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not retweet his own posts");
        if (!wallPost.addRetweet(userMail)) throw new BadRequestException("A user can not retweet two times the same post");
        User userRetweet;
        try {
            userRetweet = auxGetUser(userMail);
        } catch (NotFoundException e) {
            throw new InternalServerErrorException("Error while loading new user from the database");
        }
        WallPost retweet = new WallPost(wallPost.getDescription(), dataRetweet.getCreationDate(),null);
        retweet.setId(sequenceGeneratorService.generateSequence(WallPost.SEQUENCE_NAME));
        retweet.setRetweet(true);
        retweet.setRetweetId(wallPost.getId());
        retweet.setRetweetText(dataRetweet.getDescription());
        userRetweet.addWallPost(retweet);
        userRepository.save(creatorUser);
        if (!creatorUser.getEmail().equals(userRetweet.getEmail())) userRepository.save(userRetweet);
        else {
            WallPost auxWallPost = userRetweet.findWallPost(wallPostId);
            if (auxWallPost == null) throw new InternalServerErrorException("The user has not a wall post with this id");
            auxWallPost.addRetweet(userRetweet.getEmail());
            userRepository.save(userRetweet);
        }
    }

    @Override
    public void unretweetWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException, InternalServerErrorException {
        User creatorUser = auxGetUser(creatorMail);
        WallPost wallPost = creatorUser.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        String userMail = getLoggedUserMail();
        //if (userMail.equals(creatorMail)) throw new BadRequestException("A user can not unretweet his own posts");
        if (!wallPost.deleteRetweet(userMail)) throw new BadRequestException("A user has not a retweet in this post");
        User userRetweet;
        try {
            userRetweet = auxGetUser(userMail);
        } catch (NotFoundException e) {
            throw new InternalServerErrorException("Error while loading new user from the database");
        }
        WallPost retweet = userRetweet.findRetweet(wallPostId);
        if (retweet == null) throw new InternalServerErrorException("Error while updating database");
        userRetweet.deleteWallPost(retweet.getId());
        userRepository.save(creatorUser);
        if (!creatorUser.getEmail().equals(userRetweet.getEmail())) userRepository.save(userRetweet);
        else {
            WallPost auxWallPost = userRetweet.findWallPost(wallPostId);
            if (auxWallPost == null) throw new InternalServerErrorException("The user has not a wall post with this id");
            if (!auxWallPost.deleteRetweet(userMail)) throw new InternalServerErrorException("A user has not a retweet in this post");
            userRepository.save(userRetweet);
        }
    }

    @Override // LUEGO SE PUEDE MEJORAR LA EFICIENCIA
    public List<DataWallPostAux> GetInitialWallPosts(String email) throws NotFoundException {
        List<DataWallPostAux> result = new ArrayList<>();
        User actual_user = auxGetUser(email);
        List<String> users_emails = actual_user.getFriends().getFriends();
        users_emails.add(email);

        for(int i = 0; i < users_emails.size(); ++i) {  // Puts on result all the posts of all users
            User user = auxGetUser(users_emails.get(i));
            List<WallPost> posts = user.getWallPosts();
            for (WallPost wallPost: posts) {
                DataWallPostAux wallPostAux = new DataWallPostAux(wallPost, user.getEmail(),user.getFoto());
                result.add(wallPostAux);
            }
        }

        Collections.sort(result, Collections.reverseOrder());

        return result;
    }

    @Override
    public void deleteWallPost(long wallPostId) throws NotFoundException, InternalServerErrorException {
        String userMail = getLoggedUserMail();
        User user;
        try {
            user = auxGetUser(userMail);
        } catch (NotFoundException e) {
            throw new InternalServerErrorException("Error while loading user from the database");
        }
        WallPost wallPost = user.findWallPost(wallPostId);
        if (wallPost == null) throw new NotFoundException("The user has not a wall post with this id");
        user.deleteWallPost(wallPostId);
        if (wallPost.getRetweets().size() > 0) {
            for (String mail: wallPost.getRetweets()) {
                User aux_user;
                try {
                    aux_user = auxGetUser(mail);
                } catch (NotFoundException e) {
                    throw new InternalServerErrorException("Error while loading user from the database");
                }
                WallPost retweet = aux_user.findRetweet(wallPostId);
                if (retweet == null) throw new InternalServerErrorException("Error while updating database");
                aux_user.deleteWallPost(retweet.getId());
                userRepository.save(aux_user);
            }
        }
        userRepository.save(user);
    }



    /*
    Friends
     */

    @Override
    public List<User> getFriends(String emailUser) throws NotFoundException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(USERNOTDB);

        User user = optUser.get();

        List<String> friendEmails = user.getFriends().getFriends();
        List<User> result = new ArrayList<>();
        for(String email : friendEmails) {
            Optional<User> optUs = userRepository.findById(email);
            if(optUs.isPresent()) {
                User friend = optUs.get();
                result.add(friend);
            }
        }
        return result;
    }

    @Override
    public boolean userIsFriendWith(String email, String emailAnotherUser) throws NotFoundException {
        User actual_user = auxGetUser(email);
        User other_user = auxGetUser(emailAnotherUser);

        return actual_user.isFriend(emailAnotherUser);
    }

    @Override
    public List<User> getFriendsRequests(String emailUser) throws NotFoundException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(USERNOTDB);

        User user = optUser.get();

        List<String> friendRequestEmails = user.getFriends().getFriendRequests();
        List<User> result = new ArrayList<>();
        for(String email : friendRequestEmails) {
            Optional<User> optUs = userRepository.findById(email);
            if(optUs.isPresent()) {
                User friend = optUs.get();
                result.add(friend);
            }
        }
        return result;
    }


    @Override
    public void sendFriendRequest(String emailUser, String emailRequested) throws NotFoundException, BadRequestException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        Optional<User> optFriend = userRepository.findById(emailRequested);
        if (!optFriend.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        User friend = optFriend.get();

        if(friend.beenRequestedToBeFriendBy(emailUser)) throw new BadRequestException(ALREADY_SENT_FRIEND_REQUEST);
        if(friend.isFriend(emailUser)) throw new BadRequestException(USERS_ALREADY_ARE_FRIENDS);
        if(emailUser.equals(emailRequested)) throw new BadRequestException(ARE_THE_SAME_USER);

        friend.addFriendRequest(emailUser);

        try {
            FireMessage f = new FireMessage("Solicitud de amistad", emailUser + " quiere ser tu amigo");
            f.sendToToken(friend.getTokenFirebase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        userRepository.save(friend);
    }

    @Override
    public void acceptFriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        Optional<User> optFriend = userRepository.findById(emailRequester);
        if (!optFriend.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        User user = optUser.get();
        User friend = optFriend.get();

        if(! user.beenRequestedToBeFriendBy(emailRequester)) throw new BadRequestException(HAVENT_SENT_FRIEND_REQUEST);
        if(friend.isFriend(emailUser)) throw new BadRequestException(USERS_ALREADY_ARE_FRIENDS);

        user.addFriend(emailRequester);
        friend.addFriend(emailUser);

        user.removeFriendRequest(emailRequester);
        if(friend.beenRequestedToBeFriendBy(emailUser)) friend.removeFriendRequest(emailUser); // Quiza nunca pasara, por ahora lo pongo por si acaso

        userRepository.save(friend);
        userRepository.save(user);
    }



    @Override
    public void denyFriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        Optional<User> optFriend = userRepository.findById(emailRequester);
        if (!optFriend.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        User user = optUser.get();
        User friend = optFriend.get();

        if(! user.beenRequestedToBeFriendBy(emailRequester)) throw new BadRequestException(HAVENT_SENT_FRIEND_REQUEST);

        user.removeFriendRequest(emailRequester);
        if(friend.beenRequestedToBeFriendBy(emailUser)) friend.removeFriendRequest(emailUser); // Quiza nunca pasara, por ahora lo pongo por si acaso

        userRepository.save(friend);
        userRepository.save(user);
    }

    @Override
    public void unfriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        Optional<User> optFriend = userRepository.findById(emailRequester);
        if (!optFriend.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        User user = optUser.get();
        User friend = optFriend.get();

        if(! friend.isFriend(emailUser)) throw new BadRequestException(USERS_ARE_NOT_FRIENDS);
        if(! user.isFriend(emailRequester)) throw new BadRequestException(USERS_ARE_NOT_FRIENDS); // Solo es necesario comprobar uno


        user.removeFriend(emailRequester);
        friend.removeFriend(emailUser);

        userRepository.save(friend);
        userRepository.save(user);
    }

    @Override
    public List<User> GetUsersFriendSuggestion(String email) throws NotFoundException, BadRequestException {
        Optional<User> optUser = userRepository.findById(email);
        if (!optUser.isPresent()) throw new NotFoundException(USERNOTDB);

        User user = optUser.get();
        if(user.getPostalCode() == null) throw new BadRequestException(USER_HASNT_POSTAL_CODE);

        List<User> allUsersWithSamePostalCode= userRepository.findByPostalCode(user.getPostalCode());

        List<Integer> elementosAEliminar = new ArrayList<>();
        for(int i = 0; i < allUsersWithSamePostalCode.size(); ++i) { // Removes the users that got rejected as suggestion by the user in the past
            User u = allUsersWithSamePostalCode.get(i);
            if(user.rejectedFriendSuggestionOf(u.getEmail())) { // Si rechazo el sugerimiento
                elementosAEliminar.add(i);
            }
            else if(user.getEmail().equals(u.getEmail())) {          // Se quita a si mismo
                elementosAEliminar.add(i);
            }
            else if(u.beenRequestedToBeFriendBy(user.getEmail())) {  // El usuario sugerido tiene una solicitud pendiente por parte de *user*
                elementosAEliminar.add(i);
            }
            else if(user.beenRequestedToBeFriendBy(u.getEmail())) {  // El usuario tiene una solicitud pendiente por parte de *u*
                elementosAEliminar.add(i);
            }
            else if (u.isFriend(user.getEmail())) {  // Si ya son amigos se quita de sugeridos
                elementosAEliminar.add(i);
            }
        }

        int size = elementosAEliminar.size();
        for(int i = 0; i < size; ++i) {
            int indice = elementosAEliminar.get(i) - i; // restar i porque el vector va perdiendo elementos
            allUsersWithSamePostalCode.remove(indice);
        }

        return allUsersWithSamePostalCode;
    }

    @Override
    public void deleteFriendSuggestion(String emailUser, String emailSuggested) throws NotFoundException {
        Optional<User> optUser = userRepository.findById(emailUser);
        if (!optUser.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        Optional<User> optFriend = userRepository.findById(emailSuggested);
        if (!optFriend.isPresent()) throw new NotFoundException(ONE_OF_USERS_DONT_EXIST);

        User user = optUser.get();
        User friend = optFriend.get();

        user.addRejectedUserSuggestion(emailSuggested);
        friend.addRejectedUserSuggestion(emailUser);

        userRepository.save(friend);
        userRepository.save(user);
    }

    /*
    Event operations
     */

    @Override
    public Event createEvent(DataEvent inputEvent) throws BadRequestException, NotFoundException {
        if (!userRepository.existsById(inputEvent.getCreatorMail())) throw new NotFoundException(USERNOTDB);
        Event event = inputEvent.toEvent();
        String creatorMail = event.getCreatorMail();
        Date date = event.getDate();
        String address = event.getLocalization().getAddress();
        double longitude = event.getLocalization().getLongitude();
        double latitude = event.getLocalization().getLatitude();
        if (eventRepository.existsByCreatorMailAndDateAndLocalization_AddressAndLocalization_LongitudeAndLocalization_Latitude(creatorMail,date,address,longitude,latitude)) throw new BadRequestException("The event already exists in the database");
        eventRepository.save(event);
        return event;
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findEventsByCreator(String creatorMail) throws NotFoundException {
        if (!userRepository.existsById(creatorMail)) throw new NotFoundException(USERNOTDB);
        return eventRepository.findByCreatorMail(creatorMail);
    }

    @Override
    public List<Event> findEventsByParticipant(String participantMail) throws NotFoundException {
        if (!userRepository.existsById(participantMail)) throw new NotFoundException(USERNOTDB);
        return eventRepository.findByParticipantsInOrderByDate(participantMail);
    }

    @Override
    public CustomEventCalendarIdAdapter getUserGoogleCalendarID(String email) throws NotFoundException {
        User user = auxGetUser(email);

        boolean exist;
        String calendarID;
        List<Event> events;

        events = eventRepository.findByParticipantsInOrderByDate(email);
        calendarID = user.getGoogleCalendarID();
        exist = (calendarID != null);

        CustomEventCalendarIdAdapter customEventCalendarIdAdapter = new CustomEventCalendarIdAdapter(calendarID, exist, events);

        return customEventCalendarIdAdapter;
    }

    @Override
    public void UpdateCalendarId(String email, String calendarId) throws NotFoundException {
        User user = auxGetUser(email);
        user.setGoogleCalendarID(calendarId);
        userRepository.save(user);
    }


    @Override
    public Event updateEvent(long eventId, DataEventUpdate inputEvent) throws NotFoundException {
        Event event = auxGetEvent(eventId);
        event.setTitle(inputEvent.getTitle());
        event.setDescription(inputEvent.getDescription());
        event.setPublic(inputEvent.isPublic());
        eventRepository.save(event);
        return event;
    }

    @Override
    public void addEventParticipant(long eventId, String userMail) throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userMail)) throw new NotFoundException(USERNOTDB);
        Event event = auxGetEvent(eventId);
        if (event.userParticipates(userMail)) throw new BadRequestException("The user already participates in the even");
        event.addParticipant(userMail);
        eventRepository.save(event);
    }

    @Override
    public void removeEventParticipant(long eventId, String userMail) throws NotFoundException, BadRequestException {
        if (!userRepository.existsById(userMail)) throw new NotFoundException(USERNOTDB);
        Event event = auxGetEvent(eventId);
        if (!event.userParticipates(userMail)) throw new BadRequestException(USER_NOT_IN_EVENT);
        event.removePaticipant(userMail);
        eventRepository.save(event);
    }

    public void deleteEvent(long eventId) throws NotFoundException {
        if(!eventRepository.existsById(eventId)) throw new NotFoundException(EVENTNOTDB);
        Event event = auxGetEvent(eventId);
        List<String> participants = event.getParticipants();
        for (String particpant : participants) {
            User participantUser = auxGetUser(particpant);
            try {
                FireMessage f = new FireMessage("PetBook", "Se eliminó el evento " + event.getTitle() + " en el que participas");
                //TODO cambiar sendToToken por sendToGroup
                f.sendToToken(participantUser.getTokenFirebase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        eventRepository.deleteById(eventId);
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

    @Override
    public List<InterestSite> getAllInterestSites(boolean accepted) {
        return interestSiteRepository.findByAccepted(accepted);
    }

    @Override
    public InterestSite getInterestSite(long interestSiteId) throws NotFoundException {
        return auxGetInterestSite(interestSiteId);
    }

    @Override
    public void createInterestSite(DataInterestSite inputInterestSite) throws BadRequestException {
        if (!inputInterestSite.inputCorrect()) throw new BadRequestException("The input data is not well formed");
        if (interestSiteRepository.existsByName(inputInterestSite.getName())) throw new BadRequestException("The interest site already exists in the database");
        String name = inputInterestSite.getName();
        Localization localization = inputInterestSite.getLocalization().toLocalization();
        String description = inputInterestSite.getDescription();
        String type = inputInterestSite.getType();
        String creatorMail = getLoggedUserMail();
        InterestSite interestSite = new InterestSite(name,localization,description,type,creatorMail);
        interestSite.addVote(creatorMail);
        interestSiteRepository.save(interestSite);
    }

    @Override
    public void voteInterestSite(long interestSiteId) throws NotFoundException, BadRequestException {
        InterestSite interestSite = auxGetInterestSite(interestSiteId);
        if (interestSite.isAccepted()) throw new BadRequestException("Is only possible to vote not accepted interest sites");
        String userMail = getLoggedUserMail();
        if (interestSite.getVotes().contains(userMail)) throw new BadRequestException("The user already voted this interest site");
        interestSite.addVote(userMail);
        interestSiteRepository.save(interestSite);
    }

    @Override
    public void unVoteInterestSite(long interestSiteId) throws NotFoundException, BadRequestException {
        InterestSite interestSite = auxGetInterestSite(interestSiteId);
        if (interestSite.isAccepted()) throw new BadRequestException("Is only possible to unvote not accepted interest sites");
        String userMail = getLoggedUserMail();
        if (!interestSite.getVotes().contains(userMail)) throw new BadRequestException("The user did not vote this interest site");
        interestSite.deleteVote(userMail);
        interestSiteRepository.save(interestSite);
    }

    @Override
    public void updateInterestSite(long interestSiteId, DataInterestSiteUpdate dataInterestSiteUpdate) throws NotFoundException, BadRequestException, ForbiddenException {
        if (!dataInterestSiteUpdate.inputCorrect()) throw new BadRequestException("The input data is not well formed");
        InterestSite interestSite = auxGetInterestSite(interestSiteId);
        if (interestSite.isAccepted()) throw new BadRequestException("Is only possible to change not accepted interest sites");
        String userMail = getLoggedUserMail();
        if (!userMail.equals(interestSite.getCreatorMail())) throw new ForbiddenException("The creator user is the only one with update rights");
        String name = dataInterestSiteUpdate.getName();
        String description = dataInterestSiteUpdate.getDescription();
        String type = dataInterestSiteUpdate.getType();
        if (name != null) interestSite.setName(name);
        if (description != null) interestSite.setDescription(description);
        if (type != null) interestSite.setType(type);
        interestSiteRepository.save(interestSite);
    }

    @Override
    public void deleteInterestSite(long interestSiteId) throws NotFoundException, ForbiddenException {
        InterestSite interestSite = auxGetInterestSite(interestSiteId);
        String userMail = getLoggedUserMail();
        if (!userMail.equals(interestSite.getCreatorMail())) throw new ForbiddenException("The creator user is the only one with delete rights");
        interestSiteRepository.deleteById(interestSiteId);
    }


    /*
    Forum operations
     */

    @Override
    public List<ForumThread> getAllForumThreads() {
        return forumThreadRepository.findAllByOrderByTopicAscCreationDateDesc();
    }

    @Override
    public ForumThread getForumThread(long threadId) throws NotFoundException {
        return auxGetForumThread(threadId);
    }

    @Override
    public void createNewForumThread(DataForumThread dataForumThread) throws BadRequestException {
        String userMail = getLoggedUserMail();
        if (forumThreadRepository.existsByCreatorMailAndTitle(userMail, dataForumThread.getTitle())) throw new BadRequestException("The forum thread already exists in the database");
        ForumThread forumThread = new ForumThread(userMail,dataForumThread.getCreationDate(),dataForumThread.getTitle(),dataForumThread.getDescription(),dataForumThread.getTopic());
        forumThreadRepository.save(forumThread);
    }

    @Override
    public void updateForumThread(long threadId, DataForumThreadUpdate dataForumThreadUpdate) throws NotFoundException, ForbiddenException {
        String userMail = getLoggedUserMail();
        ForumThread forumThread = auxGetForumThread(threadId);
        if (!forumThread.getCreatorMail().equals(userMail)) throw new ForbiddenException("Only the creator user has privileges to modify a thread");
        forumThread.setDescription(dataForumThreadUpdate.getDescription());
        forumThread.setUpdateDate(dataForumThreadUpdate.getUpdateDate());
        forumThreadRepository.save(forumThread);
    }

    @Override
    public void deleteForumThread(long threadId) throws NotFoundException, ForbiddenException {
        String userMail = getLoggedUserMail();
        ForumThread forumThread = auxGetForumThread(threadId);
        if (!forumThread.getCreatorMail().equals(userMail)) throw new ForbiddenException("Only the creator user has privileges to delete a forum thread");
        forumThreadRepository.deleteById(threadId);
    }

    @Override
    public List<ForumComment> getAllThreadComments(long threadId) throws NotFoundException {
        ForumThread forumThread = auxGetForumThread(threadId);
        List<ForumComment> comments = forumThread.getComments();
        comments.sort(Comparator.comparing(ForumComment::getCreationDate));
        return comments;
    }

    @Override
    public void createForumComment(long threadId, DataForumComment dataForumComment) throws NotFoundException {
        String userMail = getLoggedUserMail();
        ForumThread forumThread = auxGetForumThread(threadId);
        ForumComment forumComment = new ForumComment(userMail,dataForumComment.getCreationDate(),dataForumComment.getDescription());
        forumComment.setId(sequenceGeneratorService.generateSequence(ForumComment.SEQUENCE_NAME));
        forumThread.addComment(forumComment);
        forumThreadRepository.save(forumThread);
    }

    @Override
    public void updateForumComment(long threadId, long commentId, DataForumCommentUpdate dataForumCommentUpdate) throws NotFoundException, ForbiddenException, BadRequestException {
        String userMail = getLoggedUserMail();
        ForumThread forumThread = auxGetForumThread(threadId);
        ForumComment forumComment = forumThread.findComment(commentId);
        if (forumComment == null) throw new NotFoundException("The forum comment does not exist in the database");
        if (!forumComment.getCreatorMail().equals(userMail)) throw new ForbiddenException("Only the creator user has privileges to modify a forum comment");
        if ((forumComment.getCreationDate().getTime() + 2*60*60*1000) < dataForumCommentUpdate.getUpdateDate().getTime()) throw new BadRequestException("Two hours have passed from the creation of the comment");
        forumComment.setUpdateDate(dataForumCommentUpdate.getUpdateDate());
        forumComment.setDescription(dataForumCommentUpdate.getDescription());
        forumComment.setUpdated(true);
        forumThreadRepository.save(forumThread);
    }

    @Override
    public void deleteForumComment(long threadId, long commentId) throws NotFoundException, ForbiddenException {
        String userMail = getLoggedUserMail();
        ForumThread forumThread = auxGetForumThread(threadId);
        ForumComment forumComment = forumThread.findComment(commentId);
        if (forumComment == null) throw new NotFoundException("The forum comment does not exist in the database");
        if (!forumComment.getCreatorMail().equals(userMail)) throw new ForbiddenException("Only the creator user has privileges to delete a forum comment");
        forumThread.getComments().remove(forumComment);
        forumThreadRepository.save(forumThread);
    }

    /**
     *
     * Search
     *
     */

    @Override
    public List<User> searchUsers(String postalCode, String petType, String userName) {
        List<User> users = new ArrayList<>();
        if(postalCode != null && userName != null)
            users = userRepository.findByPostalCodeAndFirstNameLike(postalCode, userName);   // Finds users with same postalCode and firstName like user name given
        else if(postalCode != null) // userName is null
            users = userRepository.findByPostalCode(postalCode);                         // Finds users with same postalCode
        else if(userName != null) // postalCode is null
            users = userRepository.findByFirstNameLike(userName);                        // Finds users with same firstName like user name given

        List<User> result = new ArrayList<>();

        if(petType == null) return users;
        else if(users.isEmpty() && (postalCode != null || userName != null) ) return users; // Vacio porque nadie cumple las condiciones de userName y postalCode siendo alguno de estos NO null
        else { // petType != null && !users.isEmpty()   ||   petType != null && (postalCode == null && userName == null)
            if(postalCode == null && userName == null) users = userRepository.findAll(); // petType != null && (postalCode == null && userName == null)
            for(User user : users) {                                                     // Put on result those Users who also have a pet with same pet Type as *petType*
                List<Pet> pets = mascotaRepository.findByUserEmail(user.getEmail());
                boolean hasPetWithGivenType = false;
                int i = 0;
                while(i < pets.size() && ! hasPetWithGivenType) {
                    if(pets.get(i).getEspecie() != null && pets.get(i).getEspecie().equals(petType)) {
                        hasPetWithGivenType = true;
                    }
                    ++i;
                }
                if(hasPetWithGivenType) result.add(user);
            }
        }

        return result;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void eventNotifications() {
        show_time("EventNotifications");
        List<Event> events = eventRepository.findAll();
        long day = 24*60*60*1000;
        Date now = new Date();
        for (Event event: events) {
            Date eventDate = event.getDate();
            if (eventDate.after(now) && (eventDate.getTime() < (now.getTime()+day))) {
                List<String> participants = event.getParticipants();
                for (String participant: participants) {
                    try {
                        User user = auxGetUser(participant);
                        String firebaseToken = user.getTokenFirebase();
                        FireMessage f = new FireMessage("Upcoming event", "The event with title " + event.getTitle() + " is today at " + event.getDate());
                        f.sendToToken(firebaseToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void pushDataToDatabase() {
        removeDataBase();
        String pathDataFile = "../config_files/ini_data.json";
        try(FileReader fileReader = new FileReader(pathDataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            String data = "";

            while((line = bufferedReader.readLine()) != null) {
                data = data.concat(line);
            }

            JSONObject jsonObject = new JSONObject(data);
            JSONArray users_array = jsonObject.getJSONArray("users");
            JSONArray events_array = jsonObject.getJSONArray("events");
            JSONArray interestSites_array = jsonObject.getJSONArray("interestSites");

            for (int i = 0; i < users_array.length(); ++i) {
                JSONObject user_json = users_array.getJSONObject(i);
                String dateOfBirth = user_json.getString("dateOfBirth");
                String email = user_json.getString("email");
                String firstName = user_json.getString("firstName");
                boolean mailconfirmed = user_json.getBoolean("mailconfirmed");
                String password = user_json.getString("password");
                String postalCode = user_json.getString("postalCode");
                String secondName = user_json.getString("secondName");
                //String foto = user_json.getString("foto");
                User user = new User(email, password, firstName, secondName, dateOfBirth, postalCode, mailconfirmed);
                userRepository.save(user);
            }

            for (int i = 0; i < events_array.length(); ++i) {
                JSONObject event_json = events_array.getJSONObject(i);
                String creatorMail = event_json.getString("creatorMail");
                String date_json = event_json.getString("date");
                date_json.replace("Z", "");
                String description = event_json.getString("description");
                JSONObject localization_json = event_json.getJSONObject("localization");
                String address = localization_json.getString("address");
                double latitude = localization_json.getDouble("latitude");
                double longitude = localization_json.getDouble("longitude");
                boolean isPublic = event_json.getBoolean("public");
                String title = event_json.getString("title");
                Localization localization = new Localization(address, latitude, longitude);
                String desiredStringFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
                SimpleDateFormat readingFormat = new SimpleDateFormat(desiredStringFormat);
                Date date = readingFormat.parse(date_json);
                Event event = new Event(creatorMail, localization, date, title, description, isPublic);
                eventRepository.save(event);
            }

            for (int i = 0; i < interestSites_array.length(); ++i) {
                JSONObject interest_json = interestSites_array.getJSONObject(i);
                String creatorMail = interest_json.getString("creatorMail");
                String description = interest_json.getString("description");
                JSONObject localization_json = interest_json.getJSONObject("localization");
                String address = localization_json.getString("address");
                double latitude = localization_json.getDouble("latitude");
                double longitude = localization_json.getDouble("longitude");
                String name = interest_json.getString("name");
                String type = interest_json.getString("type");
                Localization localization = new Localization(address, latitude, longitude);
                InterestSite interestSite = new InterestSite(name, localization, description, type, creatorMail);
                interestSiteRepository.save(interestSite);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }















    /*
    Auxiliary operations
     */

    private void show_time(String text) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        System.out.println(text + " -- " + hour + ":" + minute + "  " + month + "/" + day + "/" + year);
    }

    public String getLoggedUserMail() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userMail;
        try {
             userMail = (String) user;
        } catch (Exception e) {
            UserDetails userDetails = (UserDetails) user;
            userMail = userDetails.getUsername();
        }
        return userMail;
    }

    private void generateJWTToken(User user,HttpServletResponse response) {
        JwtConfig jwtConfig = new JwtConfig();
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + user.getRole());
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new java.sql.Date(now))
                .setExpiration(new java.sql.Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

    private InterestSite auxGetInterestSite(long interestSiteId) throws NotFoundException {
        Optional<InterestSite> interestSite_opt = interestSiteRepository.findById(interestSiteId);
        if (!interestSite_opt.isPresent()) throw new NotFoundException(SITENOTDB);
        return interestSite_opt.get();
    }

    private Event auxGetEvent(long eventId) throws NotFoundException {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (!optionalEvent.isPresent()) throw new NotFoundException(EVENTNOTDB);
        return optionalEvent.get();
    }

    private ForumThread auxGetForumThread(long threadId) throws NotFoundException {
        Optional<ForumThread> forumThread_opt = forumThreadRepository.findById(threadId);
        if (!forumThread_opt.isPresent()) throw new NotFoundException(THREADNOTDB);
        return forumThread_opt.get();
    }

    private User auxGetUser(String userMail) throws NotFoundException {
        Optional<User> user = userRepository.findById(userMail);
        if (!user.isPresent()) throw new NotFoundException(USERNOTDB);
        return user.get();
    }


    /*
    Test operations TODO remove this section in the future
     */

    public void removeDataBase() {
        userRepository.deleteAll();
        mascotaRepository.deleteAll();
        eventRepository.deleteAll();
        interestSiteRepository.deleteAll();
        forumThreadRepository.deleteAll();
        sequenceGeneratorService.deleteSequence(Event.SEQUENCE_NAME);
        sequenceGeneratorService.deleteSequence(ForumThread.SEQUENCE_NAME);
        sequenceGeneratorService.deleteSequence(ForumComment.SEQUENCE_NAME);
        sequenceGeneratorService.deleteSequence(WallPost.SEQUENCE_NAME);
        sequenceGeneratorService.deleteSequence(InterestSite.SEQUENCE_NAME);

    }


    @Override
    public void sendTestNotifications(String token) {

        //TO SINGLE DEVICE

        try {
            FireMessage f = new FireMessage("PRUEABA", "TEST NOTIFICATION FROM SERVER");

            //String fireBaseToken= token;
            f.sendToToken(token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
