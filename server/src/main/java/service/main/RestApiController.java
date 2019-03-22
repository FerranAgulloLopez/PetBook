package service.main;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.main.entity.User;
import service.main.repositories.UserRepository;


@RestController
@RequestMapping(value = "something")
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
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> SaveUser(@ApiParam(value="Email", required = true, example = "foo@bar.com") @RequestParam("Email") String email,
                                      @RequestBody String password) {
        userRepository.save(new User(email,password));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> GetAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(value = "/getuser/{email}", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> InfoUser(@PathVariable String email) {
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