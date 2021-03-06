package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import service.main.entity.WallPost;
import service.main.entity.input_output.ban.DataBan;
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
import service.main.service.ServerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping(value = "ServerRESTAPI")
@Api(value = "ServerRESTAPI")
public class RestApiController {

    @Autowired
    private ServerService serverService;


    /*
    Sign up and login operations
     */


    @CrossOrigin
    @PostMapping(value = "/RegisterUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "User Registration", notes = "Saves a new user to the database. It receives the user's email and password", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The user already exists")
    })
    public ResponseEntity registerUser(@ApiParam(value="A user with email and password", required = true, example = "petbook@mail.com") @RequestBody DataUser inputUser) {
        try {
            serverService.RegisterUser(inputUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/ConfirmLogin", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login Conformation", notes = "Checks if the password received as parameter is equal to the user's password. Also it returns a boolean whether the user has not confirmed his email.",tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity confirmLogin(HttpServletResponse response, @ApiParam(value="User's email", required = true, example = "petbook@mail.com") @RequestParam String email,
                                          @ApiParam(value="Password introduced", required = true, example = "1234") @RequestParam String password) {
        try {
            return new ResponseEntity<>(serverService.ConfirmLogin(email,password,response),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/SendConfirmationEmail")
    @ApiOperation(value = "Send a confirmation email", notes = "Sends to the specified user an email with the instructions to verify it.",tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The user has already verified his email"),
            @ApiResponse(code = 500, message = "Error while sending a new email")
    })
    public ResponseEntity confirmationEmail(HttpServletRequest request) {
        try {
            serverService.SendConfirmationEmail(request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
    Profile operations
     */

    @CrossOrigin
    @GetMapping(value = "/GetUser/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user by email", notes = "Get all the information of an user by its email", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity infoUser(@PathVariable String email) {
        try {
            return new ResponseEntity<>(serverService.getUserByEmail(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/update/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update all the information of the user", notes = "Updates the dateOfBirth, firstName, secondName and the postalCode of an user given its email",tags = "User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user was successfully updated"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody OutUpdateUserProfile user)
    {
        try {
            serverService.updateUserByEmail(email,user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getPicture/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get the profile picture of the user identified by email", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getPictureUser(@PathVariable String email) {
        try {
            return new ResponseEntity<>(serverService.getProfilePicture(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/setPicture/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Set the profile picture of the user identified by email", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> setPictureUser(@PathVariable String email, @RequestBody DataImage picture)
    {
        try {
            serverService.setProfilePicture(email, picture.getImage());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/token/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Set the token  of Firebase of the user identified by email", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> setTokenFirebase(@PathVariable String email, @RequestBody DataTokenFCM token)
    {
        try {
            serverService.setTokenFirebase(email, token.getToken());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/UpdatePassword/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates de the password of the user with the newPassowrd if the oldPassword is correct", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The oldPassword is not correct")
    })
    public ResponseEntity<?> updatePassword(@PathVariable String email, @RequestBody DataUpdatePassword dataUpdatePassword)
    {
        try {
            serverService.updatePassword(email, dataUpdatePassword);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    /*
    Friends operations

     */

    @CrossOrigin
    @GetMapping(value = "/GetUserFriends/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user friend's information by email", notes = "Get all the information of the friends of an user by its email. " +
            "Specifically gives the friends by the user identified by the email given in the path", tags="Friends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getFriends(@PathVariable String email) {
        try {
            return new ResponseEntity<>(serverService.getFriends(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/UserIsFriendWith/{emailAnotherUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Tells if actual user is friend with the user given in the path", notes = "Tells if actual user is friend with the user given in the path " +
            "Specifically tells if the user identified by the email given in the token and the user given in the path are friends. Returns true if are friend, false otherwise", tags="Friends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> userIsFriendWith(@PathVariable String emailAnotherUser) {
        try {
            String email = getLoggedUserMail();
            return new ResponseEntity<>(serverService.userIsFriendWith(email, emailAnotherUser), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/GetUserFriendsRequests", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user friend's information by email", notes = "Get all the information of the friends of an user by its token. " +
            "Specifically gives the friends requests received by the user identified by the email given in the token of the user", tags="Friends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getFriendsRequests() {
        try {
            String email = getLoggedUserMail();
            return new ResponseEntity<>(serverService.getFriendsRequests(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/sendFriendRequest/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Send a friend request to another user", tags="Friends", notes = "The user sends a friend request to the user identified by *friend* .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 402, message = "The user already have sent a friend request to the other user"),
            @ApiResponse(code = 403, message = "The users already are friends"),
            @ApiResponse(code = 400, message = "Users are the same users")

    })
    public ResponseEntity<?> sendFriendRequest(@PathVariable String friend)
    {
        try {

            String email = getLoggedUserMail();
            serverService.sendFriendRequest(email, friend);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            if (e.getMessage().contains("The users already are friends")) return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(401));
            if (e.getMessage().contains("user already have sent a friend request to the other user")) return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(402));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/acceptFriendRequest/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Accept a friend request from another user", tags="Friends", notes = "The user accepts a friend request from the user identified by *friend*. Then, the to users are friends.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 400, message = "The users already are friends OR The user havent sent a friend request to the other user")
    })

    public ResponseEntity<?> acceptFriendRequest(@PathVariable String friend)
    {
        try {
            String email = getLoggedUserMail();
            serverService.acceptFriendRequest(email, friend);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/denyFriendRequest/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Denies a friend request from another user", tags="Friends", notes = "The user denies a friend request from the user identified by *friend*.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 400, message = "The user *email* havent sent a friend request to the other user")
    })
    public ResponseEntity<?> denyFriendRequest(@PathVariable String friend)
    {
        try {
            String email = getLoggedUserMail();
            serverService.denyFriendRequest(email, friend);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/Unfriend/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Unfriends users", tags="Friends", notes = "The user unfriends the user identified by *friend* and vice versa.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 400, message = "The users are not friends")
    })

    public ResponseEntity<?> unfriendRequest(@PathVariable String friend)
    {
        try {
            String email = getLoggedUserMail();
            serverService.unfriendRequest(email, friend);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/GetUsersFriendSuggestion/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets users to suggest", tags="Friends", notes = "Suggests users that live in the same region as the user given")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The user has not a postal code")
    })

    public ResponseEntity<?> GetUsersFriendSuggestion(@PathVariable String email)
    {
        try {
            return new ResponseEntity<>(serverService.GetUsersFriendSuggestion(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/deleteFriendSuggestion/{email}/{emailSuggested}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Deletes a friend suggestion", tags="Friends", notes = "The user identified by *email* deletes a friend suggestion of the user identified by *emailSuggested*.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
    })
    public ResponseEntity<?> deleteFriendSuggestion(@PathVariable String email,
                                                    @PathVariable String emailSuggested)
    {
        try {
            serverService.deleteFriendSuggestion(email, emailSuggested);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /*
    Event operations
     */

    @CrossOrigin
    @PostMapping(value = "/events/CreateEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates an event", notes = "Stores an event in the database", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The event already exists in the database")
    })
    public ResponseEntity<?> createEvent(@ApiParam(value="event", required = true) @RequestBody DataEvent event) {
        try {
            return new ResponseEntity<>(serverService.createEvent(event),HttpStatus.OK);
        } catch(BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @GetMapping(value = "/events/GetAllEvents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all events", notes = "Gets all the events from the database", tags = "Events")
    public ResponseEntity<?> getAllEvents()
    {
        return new ResponseEntity<>(serverService.findAllEvents(),HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/events/GetEventsByCreator", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all the events of a specific creator", notes = "Returns all the events of the input mail creator.", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getEventsByCreator(@ApiParam(value="Creator's email", required = true, example = "petbook@mail.com") @RequestParam("mail") String email)
    {
        try {
            return new ResponseEntity<>(serverService.findEventsByCreator(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/events/GetEventsByParticipant", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all events where the input user participates", notes = "Returns all events where the input user participates. The result is ordered by the date of the event ", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getEventsByParticipant(@ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("mail") String email)
    {
        try {
            return new ResponseEntity<>(serverService.findEventsByParticipant(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/events/getUserGoogleCalendarID", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all events of the user, if has google calendar id, and the google calendar id", notes = "Returns all events of the user, if has google calendar id, and the google calendar id\n", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
    })
    public ResponseEntity<?> getUserGoogleCalendarID() throws NotFoundException {
        String email = getLoggedUserMail();
        try {
            return new ResponseEntity<>(serverService.getUserGoogleCalendarID(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/events/UpdateCalendarId/{calendarId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all events of the user, if has google calendar id, and the google calendar id", notes = "Returns all events of the user, if has google calendar id, and the google calendar id\n", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
    })
    public ResponseEntity<?> UpdateCalendarId(@PathVariable String calendarId) throws NotFoundException {
        String petbookEmail = getLoggedUserMail();
        try {
            serverService.UpdateCalendarId(petbookEmail, calendarId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PutMapping(value = "/events/UpdateEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "UPDATE Event", notes = "Updates an event. Updates only descripcion, publico and titulo. An Event is identified by any, coordenadas, dia, hora, mes, radio.", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> updateEvent(@ApiParam(value="Event's id", required = true, example = "4") @RequestParam("eventId") long eventId,
                                        @ApiParam(value="Updated parameters", required = true) @RequestBody DataEventUpdate event)
    {
        try {
            return new ResponseEntity<>(serverService.updateEvent(eventId,event),HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/events/AddEventParticipant")
    @ApiOperation(value = "Adds a user to an event", notes = "Adds a user to an event. Just add the creator's email, the coordinates, the radio and the date of the event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user or the event does not exist in the database"),
            @ApiResponse(code = 400, message = "The user already participates in the event")
    })
    public ResponseEntity<?> addEventParticipant(@ApiParam(value="Event's id", required = true, example = "4") @RequestParam("eventId") long eventId,
                                                 @ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("participantMail") String usermail)
    {
        try {
            serverService.addEventParticipant(eventId,usermail);
            return new ResponseEntity<>(HttpStatus.OK);
            //return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/events/DeleteEventParticipant")
    @ApiOperation(value = "Removes a user to an event", notes = "Removes a user to an event. Just add the creator's email, the coordinates, the radio and the date of the event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user or the event does not exist in the database"),
            @ApiResponse(code = 400, message = "The user does not participate in the event")
    })
    public ResponseEntity<?> removeEventParticipant(@ApiParam(value="Event's id", required = true, example = "4") @RequestParam("eventId") long eventId,
                                                    @ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("participantMail") String usermail)
    {
        try {
            serverService.removeEventParticipant(eventId,usermail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin
    @DeleteMapping(value = "/events/DeleteEvent")
    @ApiOperation(value = "DELETE Event", notes = "Deletes an event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> deleteEvent(@ApiParam(value="Event's id", required = true, example = "4") @RequestParam("eventId") long eventId)
    {
        try {
            serverService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    /*
    Pet operations
     */

    @CrossOrigin
    @PostMapping(value = "/CreatePet", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Pet", notes = "Stores a pet in the database.", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The pet already exists in the database")
    })
    public ResponseEntity<?> creaMascota(@ApiParam(value="Pet", required = true) @RequestBody DataPetUpdate mascota)
    {
        try {
            serverService.creaMascota(mascota);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @GetMapping(value = "/GetPet/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET Pet", notes = "Gets a pet identified by email and name ", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user or the pet does not exist in the database"),
    })
    public ResponseEntity<?> getMascota(@PathVariable String email,
                                        @ApiParam(value="Name of the pet", required = true, example = "Messi") @RequestParam String nombreMascota)
    {
        try {
            return new ResponseEntity<>(serverService.mascota_findById(email, nombreMascota),HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @GetMapping(value = "/getALLPetsByUser/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET all the Pets of a user", notes = "GET all the Pets of a user identified by email", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
    })
    public ResponseEntity<?> getAllMascotasByUser(@PathVariable String email)
    {
        try {
            return new ResponseEntity<>(serverService.findAllMascotasByUser(email),HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @PutMapping(value = "/UpdatePet/{email}/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "UPDATE Pet", notes = "Updates the information of a pet. The Pet is identified by the user email and the name given in the URL. The new data is given in DataPetUpdate(JSON). Updates everything except the user email",tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The pet does not exist in the database"),
    })
    public ResponseEntity<?> updateMascota(@PathVariable String email,
                                           @PathVariable String name,
                                           @ApiParam(value="New data of the pet", required = true) @RequestBody DataPetUpdate mascota)
    {
        try {
            serverService.updateMascota(email, name, mascota);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @DeleteMapping(value = "/DeletePet/{email}")
    @ApiOperation(value = "DELETE Pet", notes = "Deletes a pet ", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The pet does not exist in the database"),
    })
    public ResponseEntity<?> deleteMascota(@PathVariable String email,
                                           @ApiParam(value="Name of the pet", required = true, example = "Messi") @RequestParam String nombreMascota)
    {
        try {
            serverService.deleteMascota(email, nombreMascota);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }



    /*
    InterestSite operations
     */

    @CrossOrigin
    @GetMapping(value = "/interestSites", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all interest sites", notes = "Returns all the accepted interest sites or returns all the suggested ones, depending on the input parameter.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
    })
    public ResponseEntity<?> getAllInterestSite(@ApiParam(value="True to return accepted interest sites", required = true, example = "true") @RequestParam("accepted") boolean accepted) {
        return new ResponseEntity<>(serverService.getAllInterestSites(accepted), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/interestSites/{interestSiteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get one interest site", notes = "Returns the interest site identified by the specified id.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")
    })
    public ResponseEntity<?> getInterestSite(@ApiParam(value="Interest site identifier", required = true, example = "4") @PathVariable("interestSiteId") long interestSiteId) {
        try {
            return new ResponseEntity<>(serverService.getInterestSite(interestSiteId), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/interestSites", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a new interest site", notes = "Saves a new interest site to the database. It receives a json with the necessary parameters: name, localization, description and type.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The interest site already exists / The input data is not well formed"),

    })
    public ResponseEntity<?> createInterestSite(@ApiParam(value="The site parameters", required = true) @RequestBody DataInterestSite inputInterestSite) {
        try {
            serverService.createInterestSite(inputInterestSite);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/interestSites/{interestSiteId}/vote")
    @ApiOperation(value = "Vote a interest site", notes = "Votes a interest site. A interest site with more than four votes is accepted. Is only possible to vote not accepted interest sites.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The user already voted this interest site / Is only possible to vote not accepted interest sites"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")

    })
    public ResponseEntity<?> voteInterestSite(@ApiParam(value="Interest site identifier", required = true, example = "4") @PathVariable("interestSiteId") long interestSiteId) {
        try {
            serverService.voteInterestSite(interestSiteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/interestSites/{interestSiteId}/unVote")
    @ApiOperation(value = "Unvote a interest site", notes = "Unvotes a interest site. Is only possible to unvote not accepted interest sites.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The user did not vote this interest site / Is only possible to unvote not accepted interest sites"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")

    })
    public ResponseEntity<?> unVoteInterestSite(@ApiParam(value="Interest site identifier", required = true, example = "4") @PathVariable("interestSiteId") long interestSiteId) {
        try {
            serverService.unVoteInterestSite(interestSiteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PatchMapping(value = "/interestSites/{interestSiteId}")
    @ApiOperation(value = "Update a interest site", notes = "Updates a interest site. Is only possible to change not accepted interest sites", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input data is not well formed / Is only possible to change not accepted interest sites"),
            @ApiResponse(code = 401, message = "The creator user is the only one with update rights"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")

    })
    public ResponseEntity<?> updateInterestSite(@ApiParam(value="Interest site identifier", required = true, example = "4") @PathVariable("interestSiteId") long interestSiteId,
                                                @ApiParam(value="The site parameters", required = true) @RequestBody DataInterestSiteUpdate inputInterestSite) {
        try {
            serverService.updateInterestSite(interestSiteId,inputInterestSite);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/interestSites/{interestSiteId}")
    @ApiOperation(value = "Delete a interest site", notes = "Deletes a interest site.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "The creator user is the only one with delete rights"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")

    })
    public ResponseEntity<?> deleteInterestSite(@ApiParam(value="Interest site identifier", required = true, example = "4") @PathVariable("interestSiteId") long interestSiteId) {
        try {
            serverService.deleteInterestSite(interestSiteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }



    /*
    Forum operations
     */

    /*@CrossOrigin
    @PostMapping(value = "/CreateNewForumTopic")
    @ApiOperation(value = "Creates a mew topic", notes = "Adds a new topic to the database. A topic is a subject, theme, matter...", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "The topic already exists in the database")

    })
    public ResponseEntity<?> createNewForumTopic(@ApiParam(value="The topic's name", required = true) @RequestParam("name") String topicName) {
        try {
            serverService.createNewForumTopic(topicName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }*/

    @CrossOrigin
    @GetMapping(value = "/forum/GetAllForumThreads")
    @ApiOperation(value = "Returns all forum threads", notes = "Returns all the forum threads ordered by creation date and category.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")

    })
    public ResponseEntity<?> getAllForumThreads() {
        return new ResponseEntity<>(serverService.getAllForumThreads(),HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/forum/GetForumThread")
    @ApiOperation(value = "Returns a forum thread", notes = "Returns a forum thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database")

    })
    public ResponseEntity<?> getForumThread(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId) {
        try {
            return new ResponseEntity<>(serverService.getForumThread(threadId),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/forum/CreateNewForumThread")
    @ApiOperation(value = "Creates a new forum thread", notes = "Creates a new forum thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The forum thread already exists in the database")

    })
    public ResponseEntity<?> createNewForumThread(@ApiParam(value="The thread parameters", required = true) @RequestBody DataForumThread dataForumThread) {
        try {
            serverService.createNewForumThread(dataForumThread);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/forum/UpdateForumThread")
    @ApiOperation(value = "Updates a forum thread", notes = "Updates a forum thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Only the creator user has privileges to modify a forum thread"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database")

    })
    public ResponseEntity<?> updateForumThread(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId,
                                               @ApiParam(value="The thread parameters to update", required = true) @RequestBody DataForumThreadUpdate dataForumThreadUpdate) {
        try {
            serverService.updateForumThread(threadId,dataForumThreadUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/forum/DeleteForumThread")
    @ApiOperation(value = "Deletes a forum thread", notes = "Deletes a forum thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Only the creator user has privileges to delete a forum thread"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database")

    })
    public ResponseEntity<?> deleteForumThread(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId) {
        try {
            serverService.deleteForumThread(threadId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/forum/GetAllThreadComments")
    @ApiOperation(value = "Returns all the comments of a forum thread", notes = "Returns all the comments of a forum thread ordered by the creation date.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database")

    })
    public ResponseEntity<?> getAllThreadComments(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId) {
        try {
            return new ResponseEntity<>(serverService.getAllThreadComments(threadId),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/forum/CreateNewForumComment")
    @ApiOperation(value = "Creates a new forum comment", notes = "Creates a new forum comment in a specified thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database")

    })
    public ResponseEntity<?> createForumComment(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId,
                                                @ApiParam(value="The comment parameters", required = true) @RequestBody DataForumComment dataForumComment) {
        try {
            serverService.createForumComment(threadId,dataForumComment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/forum/UpdateForumComment")
    @ApiOperation(value = "Updates a forum comment", notes = "Updates a forum comment in a specified thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Two hours have passed from the creation of the comment"),
            @ApiResponse(code = 401, message = "Only the creator user has privileges to modify a forum comment"),
            @ApiResponse(code = 404, message = "The forum thread or comment do not exist in the database")

    })
    public ResponseEntity<?> updateForumComment(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId,
                                                @ApiParam(value="The comment's identifier", required = true, example = "4") @RequestParam("commentId") long commentId,
                                                @ApiParam(value="The comment parameters", required = true) @RequestBody DataForumCommentUpdate dataForumCommentUpdate) {
        try {
            serverService.updateForumComment(threadId,commentId,dataForumCommentUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/forum/DeleteForumComment")
    @ApiOperation(value = "Deletes a forum comment", notes = "Deletes a forum comment in a specified thread.", tags="Forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Only the creator user has privileges to delete a forum comment"),
            @ApiResponse(code = 404, message = "The forum thread does not exist in the database"),
            @ApiResponse(code = 404, message = "The forum comment does not exist in the database")

    })
    public ResponseEntity<?> deleteForumComment(@ApiParam(value="The thread's identifier", required = true, example = "4") @RequestParam("threadId") long threadId,
                                                @ApiParam(value="The comment's identifier", required = true, example = "4") @RequestParam("commentId") long commentId) {
        try {
            serverService.deleteForumComment(threadId,commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/users/{userMail}/WallPosts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all the user's wall posts", notes = "Returns all the user's wall posts.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getUserWallPosts(@ApiParam(value="User's email", required = true) @PathVariable("userMail") String userMail) {
        try {
            return new ResponseEntity<>(serverService.getUserWallPosts(userMail),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/users/{userMail}/WallPosts/{wallPostId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all the user's wall posts", notes = "Returns all the user's wall posts.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user does not exist in the database / The user has not a wall post with this id")
    })
    public ResponseEntity<?> getUserWallPost(@ApiParam(value="User's email", required = true) @PathVariable("userMail") String userMail,
                                             @ApiParam(value="The post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            return new ResponseEntity<>(serverService.getUserWallPost(userMail,wallPostId),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/WallPosts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a wall post", notes = "Creates a new wall post to the corresponding user in the token.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input data is not well formed")
    })
    public ResponseEntity<?> createWallPost(@ApiParam(value="The wall post parameters", required = true) @RequestBody DataWallPost dataWallPost) {
        try {
            serverService.createWallPost(dataWallPost);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/users/WallPosts/{wallPostId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates a wall post", notes = "Updates a wall post corresponding to the token's user.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input data is not well formed"),
            @ApiResponse(code = 404, message = "The user has not a wall post with this id")
    })
    public ResponseEntity<?> updateWallPost(@ApiParam(value="The post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId,
                                            @ApiParam(value="The wall post parameters", required = true) @RequestBody DataWallPostUpdate dataWallPostUpdate) {
        try {
            serverService.updateWallPost(wallPostId,dataWallPostUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/users/WallPosts/{wallPostId}")
    @ApiOperation(value = "Deletes a wall post", notes = "Deletes a wall post", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The user has not a wall post with this id")
    })
    public ResponseEntity<?> deleteWallPost(@ApiParam(value="The post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.deleteWallPost(wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/Like")
    @ApiOperation(value = "Like a wall post", notes = "Likes a wall post identified by its creatorMail and its wallPostId.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "A user can not like his own posts / A user can not like two times the same post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> likeWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                          @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.likeWallPost(creatorMail,wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/UnLike")
    @ApiOperation(value = "Unlike a wall post", notes = "Unlikes a wall post identified by its creatorMail and its wallPostId.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "A user can not unlike his own posts / The user has not a like in this post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> unlikeWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                          @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.unLikeWallPost(creatorMail,wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/Love")
    @ApiOperation(value = "Love a wall post", notes = "Loves a wall post identified by its creatorMail and its wallPostId.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "A user can not love his own posts / A user can not love two times the same post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> loveWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                          @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.loveWallPost(creatorMail,wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/UnLove")
    @ApiOperation(value = "Unlove a wall post", notes = "Unloves a wall post identified by its creatorMail and its wallPostId.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "A user can not unlove his own posts / The user has not a love in this post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> unloveWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                            @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.unloveWallPost(creatorMail,wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/Retweet")
    @ApiOperation(value = "Retweet a wall post", notes = "Retweets a wall post identified by its creatorMail and its wallPostId. Creates a new wallPost with the old description and a new text parameter.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input is not well formed / A user can not retweet his own posts / A user can not retweet two times the same post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> retweetWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                          @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId,
                                             @ApiParam(value="The retweet parameters", required = true) @RequestBody DataWallPost dataWallPost) {
        try {
            serverService.retweetWallPost(creatorMail, wallPostId, dataWallPost);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/users/{creatorMail}/WallPosts/{wallPostId}/UnRetweet")
    @ApiOperation(value = "UnRetweet a wall post", notes = "UnRetweets a wall post identified by its creatorMail and its wallPostId.", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input is not well formed / A user can not unretweet his own posts / A user has not a retweet in this post"),
            @ApiResponse(code = 404, message = "The creator user does not exist / The creator user has not a wall post with this id")
    })
    public ResponseEntity<?> retweetWallPost(@ApiParam(value="The creator's mail", required = true) @PathVariable("creatorMail") String creatorMail,
                                             @ApiParam(value="The wall post's identifier", required = true, example = "4") @PathVariable("wallPostId") long wallPostId) {
        try {
            serverService.unretweetWallPost(creatorMail, wallPostId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
    Ban operations
     */

    @CrossOrigin
    @GetMapping(value = "/reports")
    @ApiOperation(value = "Returns all open reports", notes = "Returns all open reports. Only admin users can use this method.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")

    })
    public ResponseEntity<?> getAllOpenReports() {
        return new ResponseEntity<>(serverService.getAllOpenBans(),HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/reports/{reportId}")
    @ApiOperation(value = "Returns a report", notes = "Returns a report. Only admin users can use this method.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "The report does not exist in the database")

    })
    public ResponseEntity<?> getReport(@ApiParam(value="The report's identifier", required = true, example = "4") @PathVariable("reportId") long reportId) {
        try {
            return new ResponseEntity<>(serverService.getBan(reportId),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/reports")
    @ApiOperation(value = "Creates a new report", notes = "Creates a new report which is returned in the response body.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The input data is not well formed"),
            @ApiResponse(code = 403, message = "Is not possible to create two reports on the same user by the same user"),
            @ApiResponse(code = 404, message = "The suspect user does not exist in the database")

    })
    public ResponseEntity<?> createNewReport(@ApiParam(value="The report parameters", required = true) @RequestBody DataBan dataBan) {
        try {
            return new ResponseEntity<>(serverService.createBan(dataBan),HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/reports/{reportId}/voteApprove")
    @ApiOperation(value = "Votes in favour of banning a user", notes = "Votes in favour of banning a user. A user is banned after three afirmative votes. Only admin users can use this method.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 403, message = "Is not possible to vote a closed report"),
            @ApiResponse(code = 400, message = "Is not possible to vote two times the same report"),
            @ApiResponse(code = 404, message = "The report does not exist in the database")

    })
    public ResponseEntity<?> voteApprovedReport(@ApiParam(value="The report's identifier", required = true, example = "4") @PathVariable("reportId") long reportId) {
        try {
            serverService.voteApprove(reportId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/reports/{reportId}/voteReject")
    @ApiOperation(value = "Votes against banning a user", notes = "Votes against banning a user. A report is dismissed after three reject votes. Only admin users can use this method.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 403, message = "Is not possible to vote a closed report"),
            @ApiResponse(code = 400, message = "Is not possible to vote two times the same report"),
            @ApiResponse(code = 404, message = "The report does not exist in the database")

    })
    public ResponseEntity<?> voteRejectReport(@ApiParam(value="The report's identifier", required = true, example = "4") @PathVariable("reportId") long reportId) {
        try {
            serverService.voteReject(reportId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/reports/{reportId}/unVote")
    @ApiOperation(value = "Deletes a vote", notes = "Deletes the logged user vote in one report.", tags="Reports")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "The user has not voted this report"),
            @ApiResponse(code = 403, message = "Is not possible to vote a closed report"),
            @ApiResponse(code = 404, message = "The report does not exist in the database")

    })
    public ResponseEntity<?> deleteVoteReport(@ApiParam(value="The report's identifier", required = true, example = "4") @PathVariable("reportId") long reportId) {
        try {
            serverService.removeVote(reportId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }






    /**
     *
     *
     * Search
     *
     */


    @CrossOrigin
    @GetMapping(value = "/Search/User")
    @ApiOperation(value = "Search users by postal code, type of pet and name ", notes = "Returns all the users that match the search by postal code, type of pet and name\n " +
            "The Operation does not return repeated users.", tags="Search")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    public ResponseEntity<?> searchUsers(   @ApiParam(value="Postal code",  required = false)   @RequestParam(value = "postalCode", required = false)   String  postalCode,
                                            @ApiParam(value="Pet type",     required = false)   @RequestParam(value = "petType",    required = false)   String  petType,
                                            @ApiParam(value="User's name",  required = false)   @RequestParam(value = "userName",   required = false)   String  userName)
    {
        return new ResponseEntity<>(serverService.searchUsers(postalCode, petType, userName), HttpStatus.OK);
    }


    /**
     *
     *
     * Search
     *
     */


    @CrossOrigin
    @GetMapping(value = "/WallPosts/GetInitialWallPosts")
    @ApiOperation(value = "Returns ALL posts(user+user's friends) ordered by time", notes = "Returns ALL posts made by user and user's friends. The Posts are ordered by time.\n " +
            "Ascendent order(first positions are older)", tags="WallPosts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    public ResponseEntity<?> GetInitialWallPosts()
    {
        String email = getLoggedUserMail();
        try {
            return new ResponseEntity<>(serverService.GetInitialWallPosts(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


































    /*
    Only testing purposes TODO remove this section in the future
     */

    @CrossOrigin
    @DeleteMapping(value = "/Test/RemoveDatabase")
    @ApiOperation(value = "Testing", tags = "Testing")
    public ResponseEntity<?> removeDatabase()
    {
        serverService.removeDataBase();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/Test/PushDataToDatabase")
    @ApiOperation(value = "Testing", tags = "Testing")
    public ResponseEntity<?> pushDataToDatabase()
    {
        serverService.pushDataToDatabase();
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @CrossOrigin
    @PostMapping(value = "/notification")
    @ApiOperation(value = "Send a test notification", notes = "Sends teste notification.",tags="Testing")
    @ApiResponses(value = {
    })
    public ResponseEntity<?> testNotification(@ApiParam(value="token", required = true) @RequestParam("token") String token) {
        serverService.sendTestNotifications(token);
        return new ResponseEntity<>(HttpStatus.OK);

    }



        /*
    Auxiliary operations
     */

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


}