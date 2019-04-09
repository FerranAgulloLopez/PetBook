package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.User;
import service.main.entity.output.*;
import service.main.exception.AlreadyExistsException;
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
    private ServerService serverService;


    /*
    Sign up and login operations
     */


    @CrossOrigin
    @RequestMapping(value = "/RegisterUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "User Registration", notes = "Saves a new user to the database. It receives the user's email and password", tags="User")
    public ResponseEntity<?> RegisterUser(@ApiParam(value="A user with email and password", required = true) @RequestBody User input) {
        serverService.RegisterUser(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/ConfirmLogin", method = RequestMethod.POST)
    @ApiOperation(value = "Login Conformation", notes = "Checks if the password received as parameter is equal to the user's password. Also it returns a boolean whether the user has not confirmed his email.",tags="LogIn")
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
    @ApiOperation(value = "Send a confirmation email", notes = "Sends to the specified user an email with the instructions to verify it.",tags="LogIn")
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
    @RequestMapping(value = "/GetUser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "Get user by email", notes = "Get all the information of an user by its email", tags="User")
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
    @ApiOperation(value = "Update all the information of the user", notes = "Updates the dateOfBirth, firstName, secondName and the postalCode of an user given its email",tags = "User")
    public ResponseEntity<?> UpdateUser(@PathVariable String email, @RequestBody OutUpdateUserProfile user)
    {
        try {
            serverService.updateUserByEmail(email,user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


//

    @CrossOrigin
    @RequestMapping(value = "/CreaEvento", method = {RequestMethod.POST})
    @ApiOperation(value = "Crear Evento", notes = "Guarda un evento en la base de datos.", tags = "Events")
    public ResponseEntity<?> creaEvento(@ApiParam(value="Un Evento", required = true) @RequestBody DataEvento evento) throws AlreadyExistsException, NotFoundException {
        try {
            serverService.creaEvento(evento.getUserEmail(), evento.getAny(), evento.getMes(), evento.getDia(), evento.getHora(), evento.getCoordenadas(), evento.getRadio());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FOUND);
        }
    }





        @CrossOrigin
    @RequestMapping(value = "/getALLEventos", method = RequestMethod.GET)
    @ApiOperation(value = "GET ALL Evento", notes = "Obtiene la informacion de todos los eventos ", tags = "Events")
    public ResponseEntity<?> getAllEventos()
    {
        return new ResponseEntity<>(serverService.findAllEventos(),HttpStatus.OK);

    }



    @CrossOrigin
    @RequestMapping(value = "/UpdateEvento/{email}", method = RequestMethod.PUT)
    @ApiOperation(value = "UPDATE Evento", notes = "Modifica un evento. Sirve para modificar los atributos descripcion, numero de asistentes, participantes, publico. EL Evento se identifica por any, coordenadas, dia, hora, mes, radio.", tags = "Events")
    public ResponseEntity<?> updateEvento(@PathVariable String email,
                                          @ApiParam(value="Evento", required = true) @RequestBody DataEventoUpdate evento) throws NotFoundException
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
    @RequestMapping(value = "/DeleteEvento", method = RequestMethod.DELETE)
    @ApiOperation(value = "DELETE Evento", notes = "Elimina un evento ", tags = "Events")
    public ResponseEntity<?> deleteEvento(
                                           @ApiParam(value="Evento", required = true) @RequestBody DataEvento evento) throws NotFoundException
    {
        try {
            serverService.deleteEvento(evento.getUserEmail(), evento.getAny(), evento.getMes(), evento.getDia(), evento.getHora(), evento.getCoordenadas(), evento.getRadio());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }







    @CrossOrigin
    @RequestMapping(value = "/CreaMascota", method = {RequestMethod.POST})
    @ApiOperation(value = "Crear Mascota", notes = "Guarda una mascota en la base de datos.", tags = "Pets")
    public ResponseEntity<?> creaMascota(@ApiParam(value="Mascota", required = true) @RequestBody DataMascota mascota)
    {
        try {
            serverService.creaMascota(mascota.getEmail(), mascota.getNombre());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FOUND);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "/GetMascota/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "GET Mascota", notes = "Obtiene la informacion de una mascota ", tags = "Pets")
    public ResponseEntity<?> getMascota(@PathVariable String email,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota) throws NotFoundException
    {
        try {
            return new ResponseEntity<>(serverService.mascota_findById(email, nombreMascota).get(),HttpStatus.OK);
        }
        catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin
    @RequestMapping(value = "/getALLMascotasByUser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "GET ALL Mascotas de un Usuario", notes = "Obtiene la informacion de todas las mascotas del usuario", tags = "Pets")
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
    @RequestMapping(value = "/UpdateMascota/{email}", method = RequestMethod.PUT)
    @ApiOperation(value = "UPDATE Mascota", notes = "Modifica una mascota. Sirve para modificar los atributos de la mascota. La mascota se identifica por email y nombre.",tags = "Pets")
    public ResponseEntity<?> updateMascota(@PathVariable String email,
                                          @ApiParam(value="Nuevos datos de la Mascota", required = true) @RequestBody DataMascotaUpdate mascota) throws NotFoundException
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
    @RequestMapping(value = "/DeleteMascota/{email}", method = RequestMethod.DELETE)
    @ApiOperation(value = "DELETE Mascota", notes = "Elimina una mascota ", tags = "Pets")
    public ResponseEntity<?> deleteMascota(@PathVariable String email,
                                        @ApiParam(value="Nombre de la mascota", required = true, example = "Messi") @RequestParam String nombreMascota) throws NotFoundException
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
    Only testing purposes TODO remove this section in the future
     */

    @CrossOrigin
    @RequestMapping(value = "/Test/RemoveDatabase", method = RequestMethod.DELETE)
    @ApiOperation(value = "Testing", tags = "Testing")
    public ResponseEntity<?> RemoveDatabase()
    {
        serverService.removeDataBase();
        return new ResponseEntity<>(HttpStatus.OK);
    }




}