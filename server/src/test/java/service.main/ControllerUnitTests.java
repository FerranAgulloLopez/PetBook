package service.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.main.entity.Evento;
import service.main.entity.PBKDF2Hasher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerUnitTests {

    /*
    Unit tests
     */

    @Test
    public void HashClass() {
        PBKDF2Hasher hasher = new PBKDF2Hasher();
        String password = "1234IloveParis";
        String token = hasher.hash(password.toCharArray());
        boolean result = hasher.checkPassword(password.toCharArray(), token);
        assertTrue("The hash operation is not working well",result);
    }
}
