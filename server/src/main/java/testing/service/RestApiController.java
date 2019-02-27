package testing.service;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "something")
@Api(value = "ServerRESTAPI")
public class RestApiController {

    @CrossOrigin
    @RequestMapping(value = "/Test", method = RequestMethod.GET)
    @ApiOperation(value = "Testing")
    public ResponseEntity<?> testing() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}