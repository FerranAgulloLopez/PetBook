package service.main.service;

import service.main.entity.*;
import service.main.entity.input_output.event.DataEvent;
import service.main.entity.input_output.event.DataEventUpdate;
import service.main.entity.input_output.forum.DataForumComment;
import service.main.entity.input_output.forum.DataForumCommentUpdate;
import service.main.entity.input_output.forum.DataForumThread;
import service.main.entity.input_output.forum.DataForumThreadUpdate;
import service.main.entity.input_output.image.DataImage;
import service.main.entity.input_output.interestsite.DataInterestSite;
import service.main.entity.input_output.pet.DataPetUpdate;
import service.main.entity.input_output.user.DataUser;
import service.main.entity.input_output.user.OutLogin;
import service.main.entity.input_output.user.OutUpdateUserProfile;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;
import service.main.exception.NotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public interface ServerService {



    /*
    User operations
     */

    public OutLogin ConfirmLogin(String email, String password, HttpServletResponse response) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(String email) throws NotFoundException, BadRequestException, InternalErrorException;

    public void RegisterUser(DataUser inputUser) throws BadRequestException;

    public User getUserByEmail(String email) throws NotFoundException;

    public void updateUserByEmail(String email, OutUpdateUserProfile user) throws NotFoundException;

    public DataImage getProfilePicture(String email) throws NotFoundException;

    public void setProfilePicture(String email, String picture) throws NotFoundException;

    public void setTokenFirebase(String email, String token) throws NotFoundException;


    /*
    Friends operations
     */
    List<User> getFriends(String emailUser) throws NotFoundException;
    List<User> getFriendsRequests(String emailUser) throws NotFoundException; // Gets the users who made a request to the user identified by *email*


    public void sendFriendRequest(String emailUser, String emailRequested) throws NotFoundException, BadRequestException;
    public void acceptFriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException;
    public void denyFriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException;

    public void unfriendRequest(String emailUser, String emailRequester) throws NotFoundException, BadRequestException;

    public List<User> GetUsersFriendSuggestion(String email) throws NotFoundException, BadRequestException;
    public void deleteFriendSuggestion(String emailUser, String emailSuggested) throws NotFoundException;




    /*
    Event operations
     */

    public Event createEvent(DataEvent event) throws BadRequestException, NotFoundException;

    public List<Event> findAllEvents();

    public List<Event> findEventsByCreator(String creatorMail) throws NotFoundException;

    public List<Event> findEventsByParticipant(String participantMail) throws NotFoundException;

    public Event updateEvent(long eventId, DataEventUpdate event) throws NotFoundException;

    public void addEventParticipant(long eventId, String userMail) throws NotFoundException, BadRequestException;

    public void removeEventParticipant(long eventId, String userMail) throws NotFoundException, BadRequestException;

    public void deleteEvent(long eventId) throws NotFoundException;



    /*
    Pet operations
     */

    public void creaMascota(DataPetUpdate mascota) throws BadRequestException, NotFoundException;

    public Pet mascota_findById(String emailDuenyo, String nombreMascota) throws NotFoundException;

    void updateMascota(String email, String name, DataPetUpdate mascota) throws NotFoundException;

    public void deleteMascota(String emailDuenyo, String nombreMascota) throws NotFoundException;

    public List<Pet> findAllMascotasByUser(String email) throws NotFoundException;

    public void removeDataBase();



    /*
    Interest site operations
     */

    public void createInterestSite(DataInterestSite inputInterestSite) throws BadRequestException, NotFoundException;

    public InterestSite getInterestSite(String name, String localization) throws NotFoundException;

    public void voteInterestSite(String interestSiteName, String interestSiteLocalization, String userEmail) throws NotFoundException, BadRequestException;



    /*
    Forum operations
     */

    /*public void createNewForumTopic(String topicName) throws BadRequestException;*/

    public List<ForumThread> getAllForumThreads();

    public ForumThread getForumThread(String creatorMail, String title) throws NotFoundException;

    public void createNewForumThread(DataForumThread dataForumThread) throws BadRequestException, NotFoundException;

    public void updateForumThread(String creatorMail, String title, DataForumThreadUpdate dataForumThreadUpdate) throws NotFoundException;

    public void deleteForumThread(String creatorMail, String title) throws NotFoundException;

    public List<ForumComment> getAllThreadComments(String creatorMail, String title) throws NotFoundException;

    public void createForumComment(String creatorMail, String title, DataForumComment dataForumComment) throws NotFoundException, BadRequestException;

    public void updateForumComment(String threadCreatorMail, String threadTitle, String commentCreatorMail, Date commentCreationDate, DataForumCommentUpdate dataForumCommentUpdate) throws NotFoundException;

    public void deleteForumComment(String threadCreatorMail, String threadTitle, String commentCreatorMail, Date commentCreationDate) throws NotFoundException;


    public void sendTestNotifications(String token);
}
