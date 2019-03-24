package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.User;
import service.main.exception.NotFoundException;
import service.main.repositories.UserRepository;
import service.main.service.ServerService;


@RestController
@RequestMapping(value = "ServerRESTAPI")
@Api(value = "ServerRESTAPI")
public class RestApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServerService serverService;


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
    @RequestMapping(value = "/ConfirmLogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Login Conformation", notes = "Checks if the password received as parameter is equal to the user's password. Also it returns a boolean whether the user has not confirmed his email.")
    public ResponseEntity<?> ConfirmLogin(@ApiParam(value="User's email", required = true) @RequestParam String email,
                                          @ApiParam(value="Password introduced", required = true) @RequestParam String password) {
        try {
            boolean result = serverService.ConfirmLogin(email, password);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
    Profile operations
     */

    @CrossOrigin
    @RequestMapping(value = "/GetNames", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> SecondName() {
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/GetUser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> InfoUser(@PathVariable String email) {
        //TODO si no existe el usuario devolver una excepción o algo
        return new ResponseEntity<>(userRepository.findById(email).get().getEmail(),HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/update/{email}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> UpdateUser(@PathVariable String email, @RequestBody User user)
    {
        userRepository.deleteById(email);
        userRepository.insert(user);
        //userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
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

}