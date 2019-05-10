package service.main;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import service.main.entity.Image;

import java.io.*;

import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(username = "test", password = "test", roles = "USER")
public class ControllerImageTests {

    private String path = "../testing_files/Fotos/";

    private String inputFilePath  = path + "RealMadrid.png";
    private String outputFilePath = path + "RealMadrid_prueba.png";

    @Test
    public void guardaJSONImagenUsandoBase64() throws Exception {

        Image foto = new Image(inputFilePath);

        JSONObject json = new JSONObject();
        json.accumulate("foto", foto.getImagen());  // En String

        String encodedStringOutput = json.getString("foto");

        File outputFile = new File(outputFilePath);
        byte[] decodedBytes = Base64.decodeBase64(encodedStringOutput);
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

        assertTrue(FileUtils.contentEquals(foto.getFile(), outputFile));
    }


    @Test
    public void fileToBase64StringConversion() throws IOException {

        File inputFile = new FileSystemResource(inputFilePath).getFile();

        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64
                .encodeBase64URLSafeString(fileContent);


        // create output file
        File outputFile = new File(outputFilePath);

        // decode the string and write to file
        byte[] decodedBytes = Base64
                .decodeBase64(encodedString);
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

        assertTrue(FileUtils.contentEquals(inputFile, outputFile));
    }
}
