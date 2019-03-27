package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.User;
import service.main.exception.BadRequestException;
import service.main.exception.NotFoundException;
import service.main.repositories.EventoRepository;
import service.main.repositories.UserRepository;
import service.main.service.ServerService;

import java.util.Calendar;
import java.util.Optional;


@RestController
@RequestMapping(value = "ServerRESTAPI")
@Api(value = "ServerRESTAPI")
public class RestApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServerService serverService;

    @Autowired
    private EventoRepository eventoRepository;


    /*
    Sign up and login operations
     */

    @CrossOrigin
    @RequestMapping(value = "/RegisterUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "User Registration", notes = "Saves a new user to the database. It receives the user's email and password")
    public ResponseEntity<?> RegisterUser(@ApiParam(value="A user with email and password", required = true) @RequestBody User input) {
        serverService.RegisterUser(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/ConfirmLogin", method = RequestMethod.POST)
    @ApiOperation(value = "Login Conformation", notes = "Checks if the password received as parameter is equal to the user's password. Also it returns a boolean whether the user has not confirmed his email.")
    public ResponseEntity<?> ConfirmLogin(@ApiParam(value="User's email", required = true, example = "petbook@mail.com") @RequestParam String email,
                                          @ApiParam(value="Password introduced", required = true, example = "1234") @RequestParam String password) {
        try {
            return new ResponseEntity<>(serverService.ConfirmLogin(email,password),HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/SendConfirmationEmail", method = RequestMethod.POST)
    @ApiOperation(value = "Send a confirmation email", notes = "Sends to the specified user an email with the instructions to verify it.")
    public ResponseEntity<?> ConfirmationEmail(@ApiParam(value="User's email", required = true, example = "petbook@mail.com") @RequestParam String email) {
        try {
            serverService.SendConfirmationEmail(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /*
    Profile operations
     */

    @CrossOrigin
    @RequestMapping(value = "/GetAllUsers", method = RequestMethod.GET)
    @ApiOperation(value = "Get all usuarios")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/GetUser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "Get user by email", notes = "Get all the information of an user by its email.")
    public ResponseEntity<?> InfoUser(@PathVariable String email) {
        try {
            return new ResponseEntity<>(serverService.getUserByEmail(email), HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/update/{email}", method = RequestMethod.POST)
    @ApiOperation(value = "Update all the information of the user")
    public ResponseEntity<?> UpdateUser(@PathVariable String email, @RequestBody User user)
    {
        try {
            serverService.updateUserByEmail(email,user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




    /*
    Only testing purposes TODO remove this section in the future
     */

    @CrossOrigin
    @RequestMapping(value = "/Test/RemoveDatabase", method = RequestMethod.DELETE)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> RemoveDatabase()
    {
        userRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }









    // Evento
    //@RequestMapping(value = "/CreaEvento", method = {RequestMethod.POST,RequestMethod.GET})


    @CrossOrigin
    @RequestMapping(value = "/CreaEvento", method = {RequestMethod.POST})
    @ApiOperation(value = "Crear Evento", notes = "Guarda un evento en la base de datos. ")
    public ResponseEntity<?> creaEvento(@ApiParam(value="Email del usuario creador", required = true, example = "petbook@mail.com") @RequestParam String userEmail,
                                        @ApiParam(value="Año del evento", required = true, example = "2019") @RequestParam Integer any,
                                        @ApiParam(value="Mes del evento", required = true, example = "12") @RequestParam Integer mes,
                                        @ApiParam(value="Dia dentro del mes del evento", required = true, example = "31") @RequestParam Integer dia,
                                        @ApiParam(value="Hora del evento, Formato 24 horas", required = true, example = "16") @RequestParam Integer hora,
                                        @ApiParam(value="Coordenadas", required = true, example = "2") @RequestParam Integer coordenadas,
                                        @ApiParam(value="Radio", required = true, example = "2") @RequestParam Integer radio)
    {

            serverService.creaEvento(userEmail, any, mes, dia, hora, coordenadas, radio);
            return new ResponseEntity<>(HttpStatus.OK);


    }

    @CrossOrigin
    @RequestMapping(value = "/getALLEventos", method = RequestMethod.GET)
    @ApiOperation(value = "GET ALL Evento", notes = "Obtiene la informacion de todos los eventos ")
    public ResponseEntity<?> getAllEventos()
    {
        return new ResponseEntity<>(serverService.findAllEventos(),HttpStatus.OK);

    }







    @CrossOrigin
    @RequestMapping(value = "/CreaMascota", method = {RequestMethod.POST})
    @ApiOperation(value = "Crear Mascota", notes = "Guarda una mascota en la base de datos.")
    public ResponseEntity<?> creaMascota(@ApiParam(value="Email del dueño", required = true, example = "RealMadrid@mail.com") @RequestParam String emailDuenyo,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota)
    {

        serverService.creaMascota(emailDuenyo, nombreMascota);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/GetMascota", method = RequestMethod.GET)
    @ApiOperation(value = "GET Mascota", notes = "Obtiene la informacion de una mascota ")
    public ResponseEntity<?> getMascota(@ApiParam(value="Email del dueño", required = true, example = "RealMadrid@mail.com") @RequestParam String emailDuenyo,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota)
    {
        return new ResponseEntity<>(serverService.mascota_findById(emailDuenyo, nombreMascota).get(),HttpStatus.OK);

    }

    @CrossOrigin
    @RequestMapping(value = "/getALLMascotas", method = RequestMethod.GET)
    @ApiOperation(value = "GET ALL Mascotas", notes = "Obtiene la informacion de todas las mascotas ")
    public ResponseEntity<?> getAllMascotas()
    {
        return new ResponseEntity<>(serverService.findAllMascotas(),HttpStatus.OK);

    }




    @CrossOrigin
    @RequestMapping(value = "/DeleteMascota", method = RequestMethod.DELETE)
    @ApiOperation(value = "DELETE Mascota", notes = "Elimina una mascota ")
    public ResponseEntity<?> deleteMascota(@ApiParam(value="Email del dueño", required = true, example = "RealMadrid@mail.com") @RequestParam String emailDuenyo,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota)
    {
        serverService.deleteMascota(emailDuenyo, nombreMascota);
        return new ResponseEntity<>(HttpStatus.OK);

    }





}