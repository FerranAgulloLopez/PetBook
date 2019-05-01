package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.input_output.*;
import service.main.exception.BadRequestException;
import service.main.exception.InternalErrorException;
import service.main.exception.NotFoundException;
import service.main.service.ServerService;


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
            @ApiResponse(code = 400, message = "The user already exists")
    })
    public ResponseEntity<?> registerUser(@ApiParam(value="A user with email and password", required = true, example = "petbook@mail.com") @RequestBody DataUser inputUser) {
        try {
            serverService.RegisterUser(inputUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/ConfirmLogin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login Conformation", notes = "Checks if the password received as parameter is equal to the user's password. Also it returns a boolean whether the user has not confirmed his email.",tags="LogIn")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> confirmLogin(@ApiParam(value="User's email", required = true, example = "petbook@mail.com") @RequestParam String email,
                                          @ApiParam(value="Password introduced", required = true, example = "1234") @RequestParam String password) {
        try {
            return new ResponseEntity<>(serverService.ConfirmLogin(email,password),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/SendConfirmationEmail")
    @ApiOperation(value = "Send a confirmation email", notes = "Sends to the specified user an email with the instructions to verify it.",tags="LogIn")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The user has already verified his email"),
            @ApiResponse(code = 500, message = "Error while sending a new email")
    })
    public ResponseEntity<?> confirmationEmail(@ApiParam(value="User's email", required = true, example = "petbook@mail.com") @RequestParam String email) {
        try {
            serverService.SendConfirmationEmail(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
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
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> infoUser(@PathVariable String email) {
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
            @ApiResponse(code = 404, message = "The user does not exist in the database or does not have profile picture")
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

    /*
    Friends operations

     */

    @CrossOrigin
    @GetMapping(value = "/GetUserFriends/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user friend's information by email", notes = "Get all the information of the friends of an user by its email. " +
            "Specifically gives the friends by the user identified by the email given in the path", tags="User")
    @ApiResponses(value = {
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
    @GetMapping(value = "/GetUserFriendsRequests/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get user friend's information by email", notes = "Get all the information of the friends of an user by its email. " +
            "Specifically gives the friends requests received by the user identified by the email given in the path", tags="User")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getFriendsRequests(@PathVariable String email) {
        try {
            return new ResponseEntity<>(serverService.getFriendsRequests(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/sendFriendRequest/{email}/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Send a friend request to another user", tags="User", notes = "The user identified by *email* sends a friend request to the user identified by *friend* .")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 400, message = "The user already have sent a friend request to the other user OR The users already are friends")
    })
    public ResponseEntity<?> sendFriendRequest(@PathVariable String email,
                                               @PathVariable String friend)
    {
        try {
            serverService.sendFriendRequest(email, friend);
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
    @PostMapping(value = "/acceptFriendRequest/{email}/{friend}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Accept a friend request from another user", tags="User", notes = "The user identified by *email* accepts a friend request from the user identified by *friend*. Then, the to users are friends.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "One of the users does not exist in the database"),
            @ApiResponse(code = 400, message = "The users already are friends OR The user *email* havent sent a friend request to the other user")
    })

    public ResponseEntity<?> acceptFriendRequest(@PathVariable String email,
                                                 @PathVariable String friend)
    {
        try {
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



    /*
    Event operations
     */

    @CrossOrigin
    @PostMapping(value = "/CreateEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates an Event", notes = "Stores an event in the database", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The event already exists in the database")
    })
    public ResponseEntity<?> creaEvento(@ApiParam(value="event", required = true) @RequestBody DataEventUpdate evento) {
        try {
            serverService.creaEvento(evento);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @GetMapping(value = "/getALLEvents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET ALL Evento", notes = "Gets all the events from the database", tags = "Events")
    public ResponseEntity<?> getAllEventos()
    {
        return new ResponseEntity<>(serverService.findAllEventos(),HttpStatus.OK);

    }

    @CrossOrigin
    @GetMapping(value = "/getEventsByCreator", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all the events of a specific creator", notes = "Returns all the events of the input mail creator.", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getEventsByCreator(@ApiParam(value="Creator's email", required = true, example = "petbook@mail.com") @RequestParam("email") String email)
    {
        try {
            return new ResponseEntity<>(serverService.findEventsByCreator(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getEventsByParticipant", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns all events where the input user participates", notes = "Returns all events where the input user participates. The result is ordered by the date of the event ", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> getEventsByParticipant(@ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("email") String email)
    {
        try {
            return new ResponseEntity<>(serverService.findEventsByParticipant(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping(value = "/UpdateEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "UPDATE Event", notes = "Updates an event. Updates only descripcion, publico and titulo. An Event is identified by any, coordenadas, dia, hora, mes, radio.", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> updateEvento(@ApiParam(value="event", required = true) @RequestBody DataEventUpdate evento)
    {
        try {
            serverService.updateEvento(evento);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/addEventParticipant", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Adds a user to an event", notes = "Adds a user to an event. Just add the creator's email, the coordinates, the radio and the date of the event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user or the event does not exist in the database"),
            @ApiResponse(code = 400, message = "The user already participates in the event")
    })
    public ResponseEntity<?> addEventParticipant(@ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("participantemail") String usermail,
                                                 @ApiParam(value="event", required = true) @RequestBody DataEvent evento)
    {
        try {
            serverService.addEventParticipant(usermail,evento.getUserEmail(),evento.getCoordenadas(),evento.getRadio(),evento.getFecha());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/removeEventParticipant", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Removes a user to an event", notes = "Removes a user to an event. Just add the creator's email, the coordinates, the radio and the date of the event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user or the event does not exist in the database"),
            @ApiResponse(code = 400, message = "The user does not participate in the event")
    })
    public ResponseEntity<?> removeEventParticipant(@ApiParam(value="Participant's email", required = true, example = "petbook@mail.com") @RequestParam("participantemail") String usermail,
                                                    @ApiParam(value="event", required = true) @RequestBody DataEvent evento)
    {
        try {
            serverService.removeEventParticipant(usermail,evento.getUserEmail(),evento.getCoordenadas(),evento.getRadio(),evento.getFecha());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CrossOrigin
    @DeleteMapping(value = "/DeleteEvent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "DELETE Event", notes = "Deletes an event", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> deleteEvento(@ApiParam(value="Event", required = true) @RequestBody DataEvent event)
    {
        try {
            serverService.deleteEvento(event);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /*
    Pets operations
     */

    @CrossOrigin
    @PostMapping(value = "/CreatePet", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create Pet", notes = "Stores a pet in the database.", tags = "Pets")
    @ApiResponses(value = {
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
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
    })
    public ResponseEntity<?> getAllMascotasByUser(@PathVariable String email) throws Exception
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
    @PostMapping(value = "/SuggestInterestSite", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Suggest a new interest site", notes = "Saves a new interest site to the database. It receives a json with the necessary parameters: name, localization, description, type and the " +
            "creator's email.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "The interest site already exists"),
            @ApiResponse(code = 404, message = "The user does not exist in the database")

    })
    public ResponseEntity<?> createInterestSite(@ApiParam(value="The site parameters", required = true) @RequestBody DataInterestSite inputInterestSite) {
        try {
            serverService.createInterestSite(inputInterestSite);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/GetInterestSite", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get one interest site", notes = "Get the interest site identified by the specified name and localization.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")
    })
    public ResponseEntity<?> getInterestSite(@ApiParam(value="Name of the interest site", required = true, example = "Goddard Veterinary") @RequestParam("name") String name,
                                             @ApiParam(value="Localization of the interest site", required = true, example = "00") @RequestParam("localization") String localization) {
        try {
            return new ResponseEntity<>(serverService.getInterestSite(name,localization), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/VoteInterestSite")
    @ApiOperation(value = "Vote a interest site", notes = "Votes a interest site. A interest site with more than five votes is accepted.", tags="Interest Sites")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "The user already voted this interest site"),
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 404, message = "The interest site does not exist in the database")

    })
    public ResponseEntity<?> voteInterestSite(@ApiParam(value="The interest site's name", required = true) @RequestParam("name") String interestSiteName,
                                              @ApiParam(value="The interest site's localization", required = true) @RequestParam("localization") String interestSiteLocalization,
                                              @ApiParam(value="The interest site's name", required = true) @RequestParam("email") String userEmail) {
        try {
            serverService.voteInterestSite(interestSiteName,interestSiteLocalization,userEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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




}