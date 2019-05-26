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
import service.main.entity.input_output.interestsite.DataInterestSiteUpdate;
import service.main.entity.input_output.pet.DataPetUpdate;
import service.main.entity.input_output.user.*;
import service.main.exception.BadRequestException;
import service.main.exception.ForbiddenException;
import service.main.exception.InternalServerErrorException;
import service.main.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ServerService {



    /*
    User operations
     */

    public OutLogin ConfirmLogin(String email, String password, HttpServletResponse response) throws NotFoundException;

    public void ConfirmEmail(String email) throws NotFoundException;

    public void SendConfirmationEmail(HttpServletRequest request) throws BadRequestException, InternalServerErrorException;

    public void RegisterUser(DataUser inputUser) throws BadRequestException;

    public User getUserByEmail(String email) throws NotFoundException;

    public void updateUserByEmail(String email, OutUpdateUserProfile user) throws NotFoundException;

    public DataImage getProfilePicture(String email) throws NotFoundException;

    public void setProfilePicture(String email, String picture) throws NotFoundException;

    public void setTokenFirebase(String email, String token) throws NotFoundException;



    /*
    Wall Post operations
     */

    public List<WallPost> getUserWallPosts(String userMail) throws NotFoundException;

    public void createWallPost(DataWallPost dataWallPost) throws InternalServerErrorException, BadRequestException;

    public void updateWallPost(long wallPostId, DataWallPostUpdate dataWallPostUpdate) throws NotFoundException, InternalServerErrorException, BadRequestException;

    public void deleteWallPost(long wallPostId) throws NotFoundException, InternalServerErrorException;

    public void likeWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException;

    public void unLikeWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException;

    public void loveWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException;

    public void unloveWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException;

    public void retweetWallPost(String creatorMail, long wallPostId, DataWallPost dataRetweet) throws NotFoundException, BadRequestException, InternalServerErrorException;

    public void unretweetWallPost(String creatorMail, long wallPostId) throws NotFoundException, BadRequestException, InternalServerErrorException;



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



    /*
    Interest site operations
     */

    public List<InterestSite> getAllInterestSites(boolean accepted);

    public InterestSite getInterestSite(long interestSiteId) throws NotFoundException;

    public void createInterestSite(DataInterestSite inputInterestSite) throws BadRequestException;

    public void voteInterestSite(long interestSiteId) throws NotFoundException, BadRequestException;

    public void unVoteInterestSite(long interestSiteId) throws NotFoundException, BadRequestException;

    public void updateInterestSite(long interestSiteId, DataInterestSiteUpdate dataInterestSiteUpdate) throws NotFoundException, BadRequestException, ForbiddenException;

    public void deleteInterestSite(long interestSiteId) throws NotFoundException, ForbiddenException;



    /*
    Forum operations
     */

    /*public void createNewForumTopic(String topicName) throws BadRequestException;*/

    public List<ForumThread> getAllForumThreads();

    public ForumThread getForumThread(long threadId) throws NotFoundException;

    public void createNewForumThread(DataForumThread dataForumThread) throws BadRequestException;

    public void updateForumThread(long threadId, DataForumThreadUpdate dataForumThreadUpdate) throws NotFoundException, ForbiddenException;

    public void deleteForumThread(long threadId) throws NotFoundException, ForbiddenException;

    public List<ForumComment> getAllThreadComments(long threadId) throws NotFoundException;

    public void createForumComment(long threadId, DataForumComment dataForumComment) throws NotFoundException;

    public void updateForumComment(long threadId, long commentId, DataForumCommentUpdate dataForumCommentUpdate) throws NotFoundException, ForbiddenException, BadRequestException;

    public void deleteForumComment(long threadId, long commentId) throws NotFoundException, ForbiddenException;


    /*
    Notifications
     */

    public void sendTestNotifications(String token);

    public void updatePassword(String email, DataUpdatePassword dataUpdatePassword) throws NotFoundException, BadRequestException;



    /* Search */
    public List<User> searchUsers(String postalCode, String petType, String userName);

    /*
    Test
     */

    public void removeDataBase();

    public void pushDataToDatabase();


}
