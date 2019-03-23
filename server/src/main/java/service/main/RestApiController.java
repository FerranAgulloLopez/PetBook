package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.User;
import service.main.repositories.UserRepository;


@RestController
@RequestMapping(value = "ServerRESTAPI")
@Api(value = "ServerRESTAPI")
public class RestApiController {

    @Autowired
    private UserRepository userRepository;

    @CrossOrigin
    @RequestMapping(value = "/SaveName", method = RequestMethod.POST)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> SaveName(@ApiParam(value="FirstName", required = true, example = "Pepe") @RequestParam("FirstName") String FirstName,
                                    @ApiParam(value="SecondName", required = true, example = "Rodrigo") @RequestParam("SecondName") String SecondName) {
        userRepository.save(new User(FirstName,SecondName));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/GetNames", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> SecondName() {
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/RegisterUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> SaveUser(@ApiParam(value="A user with email and password", required = true) @RequestBody User input) {
        userRepository.save(new User(input.getEmail(),input.getPassword()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/GetUser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> InfoUser(@PathVariable String email) {
        //TODO si no existe el usuario devolver una excepción o algo
        return new ResponseEntity<>(userRepository.findById(email),HttpStatus.OK);
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

}