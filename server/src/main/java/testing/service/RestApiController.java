package testing.service;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testing.service.entity.User;


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


}