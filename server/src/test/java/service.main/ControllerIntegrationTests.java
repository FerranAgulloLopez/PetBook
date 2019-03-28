package service.main;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.main.exception.AlreadyExistsException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }


    /*
    Register User operation
     */

    @Test
    public void RegisterUser() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"register_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/petbook@main.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"register_operation/output_register.json")));
    }

    /*
    Login confirmation operation
     */

    @Test
    public void LoginConfirmationOK() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"login_confirmation_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/ConfirmLogin").contentType(MediaType.APPLICATION_JSON).param("email","petbook@main.com").param("password", "believe_on_me"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"login_confirmation_operation/output_OK.json")));
    }

    @Test
    public void LoginConfirmationNOTOK() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"login_confirmation_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/ConfirmLogin").contentType(MediaType.APPLICATION_JSON).param("email","petbook@main.com").param("password", "another_one"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"login_confirmation_operation/output_NOTOK.json")));
    }

    @Test
    public void LoginConfirmationUserNOTINDB() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/ConfirmLogin").contentType(MediaType.APPLICATION_JSON).param("email","petbook@main.com").param("password", "another_one"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Confirm email operation
     */
    @Test
    public void EmailConfirmation() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"confirm_email_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/mailconfirmation/petbook@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/petbook@main.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"confirm_email_operation/output_confirm_email.json")));
    }


    /*
    Get user by email operation
     */
    @Test
    public void getUserByEmail() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getUserByEmail_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"getUserByEmail_operation/output_getUser.json")));
    }

    @Test
    public void getUserByEmailNOTINDB() throws Exception {
        this.mockMvc.perform(post("/ServerRestAPI/GetUser/noUser@petbook.com")).andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Update user by email operation
     */

    @Test
    public void updateUserByEmail() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"updateUserByEmail_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/update/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"updateUserByEmail_operation/input_updateUser.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"updateUserByEmail_operation/output_getUser.json")));
    }


    @Test
    public void updateUserByEmailNOTINDB() throws Exception {
        this.mockMvc.perform(post("/ServerRestAPI/update/noUser@petbook.com")).andDo(print()).andExpect(status().isNotFound());
    }





    @Test
    public void creaYgetALLEvento() throws Exception {

        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"crea_evento_operation/output_crea_evento.json")));
    }

    @Test
    public void creaEventoPeroJaExisteix() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isFound());
    }


    @Test
    public void creaYgetMascota() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetMascota/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"crea_mascota_operation/output_crea.json")));
    }

    @Test
    public void creaMascotaPeroJaExisteix() throws AlreadyExistsException, Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isFound());
    }

    @Test
    public void getAllMascotasBySameUser() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_crea1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_crea2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLMascotasByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"getAll_mascotaBySameUser_operation/output_crea.json")));
    }

    @Test
    public void getAllMascotasbyUserANDUserDontExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/getALLMascotasByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getMascotaANDMascotaDontExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/GetMascota/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void deleteMascota() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaMascota").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLMascotasByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_mascota_operation/output_visualiza1.json")));
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteMascota/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLMascotasByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_mascota_operation/output_visualiza2.json")));
    }

    @Test
    public void deleteMascotaANDMascotaDontExists() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteMascota/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isNotFound());
    }









    /*
    Auxiliary operations
     */

    private String read_file(String path) throws Exception {
        String result = "";
        String line = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                result = result.concat(line);
            }
            bufferedReader.close();
            JSONObject aux = new JSONObject(result);
            return aux.toString();
        } finally {
            if (fileReader != null) fileReader.close();
            if (bufferedReader != null) bufferedReader.close();
        }
    }

    private String read_file_raw(String path) throws Exception {
        String result = "";
        String line = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                result = result.concat(line);
            }
            bufferedReader.close();
            return result;
        } finally {
            if (fileReader != null) fileReader.close();
            if (bufferedReader != null) bufferedReader.close();
        }
    }
}
