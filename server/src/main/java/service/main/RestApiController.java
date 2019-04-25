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
    public ResponseEntity<?> registerUser(@ApiParam(value="A user with email and password", required = true) @RequestBody DataUser inputUser) {
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
            @ApiResponse(code = 404, message = "The user does not exist in the database")
    })
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody OutUpdateUserProfile user)
    {
        try {
            serverService.updateUserByEmail(email,user);
            return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> setPictureUser(@PathVariable String email, @RequestBody String picture)
    {
        try {
            serverService.setProfilePicture(email, picture);
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
    @PostMapping(value = "/CreaEvento", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Crear Evento", notes = "Guarda un evento en la base de datos.", tags = "Events")
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
    @GetMapping(value = "/getALLEventos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET ALL Evento", notes = "Obtiene la informacion de todos los eventos ", tags = "Events")
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
    @PutMapping(value = "/UpdateEvento/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "UPDATE Evento", notes = "Modifica un evento. Sirve para modificar los atributos descripcion, publico y titulo. EL Evento se identifica por any, coordenadas, dia, hora, mes, radio.", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> updateEvento(@PathVariable String email,
                                          @ApiParam(value="event", required = true) @RequestBody DataEventUpdate evento)
    {
        try {
            serverService.updateEvento(email, evento);
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
    @DeleteMapping(value = "/DeleteEvento", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "DELETE Evento", notes = "Deletes an event ", tags = "Events")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The event does not exist in the database")
    })
    public ResponseEntity<?> deleteEvento(@ApiParam(value="Evento", required = true) @RequestBody DataEvent event)
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
    @PostMapping(value = "/CreaMascota", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Crear Mascota", notes = "Stores a pet in the database.", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user does not exist in the database"),
            @ApiResponse(code = 400, message = "The pet already exists in the database")
    })
    public ResponseEntity<?> creaMascota(@ApiParam(value="Mascota", required = true) @RequestBody DataPetUpdate mascota)
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
    @GetMapping(value = "/GetMascota/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET Mascota", notes = "Obtiene la informacion de una mascota ", tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The user or the pet does not exist in the database"),
    })
    public ResponseEntity<?> getMascota(@PathVariable String email,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota)
    {
        try {
            return new ResponseEntity<>(serverService.mascota_findById(email, nombreMascota),HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @GetMapping(value = "/getALLMascotasByUser/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET ALL Mascotas de un Usuario", notes = "Obtiene la informacion de todas las mascotas del usuario", tags = "Pets")
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
    @PutMapping(value = "/UpdateMascota/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "UPDATE Mascota", notes = "Modifica una mascota. Sirve para modificar los atributos de la mascota. La mascota se identifica por email y nombre.",tags = "Pets")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The pet does not exist in the database"),
    })
    public ResponseEntity<?> updateMascota(@PathVariable String email,
                                          @ApiParam(value="Nuevos datos de la Mascota", required = true) @RequestBody DataPetUpdate mascota)
    {
        try {
            serverService.updateMascota(email, mascota);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @DeleteMapping(value = "/DeleteMascota/{email}")
    @ApiOperation(value = "DELETE Mascota", notes = "Deletes a pet ", tags = "Pets")
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