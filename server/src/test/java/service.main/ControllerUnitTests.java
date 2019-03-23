package service.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.main.entity.Evento;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerUnitTests {

    /*
    Unit tests
     */

    @Test
    public void FuncionRandom() throws Exception {
        Evento event = new Evento();
        String aux = event.funcion_auxiliar_provar_random("dads");
        assertEquals(aux, "yea");
    }
}
