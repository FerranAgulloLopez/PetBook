package service.main;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.main.entity.Image;

import java.io.FileWriter;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", password = "test", roles = "USER")
public class ControllerPetsTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/pets/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void creaYgetMascota() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetPet/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"crea_mascota_operation/output_crea.json")));
    }

    @Test
    public void creaMascotaPeroJaExisteix() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllMascotasBySameUser() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_crea1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getAll_mascotaBySameUser_operation/input_crea2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLPetsByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"getAll_mascotaBySameUser_operation/output_crea.json")));
    }

    @Test
    public void getAllMascotasbyUserANDUserDontExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/getALLPetsByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getMascotaANDMascotaDontExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/GetPet/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    public void updateMascota() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "update_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "update_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetPet/foo@mail.com?nombreMascota=Messi"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "update_mascota_operation/output_crea.json")));


        Image foto = new Image(inputFilePath);

        JSONObject json = new JSONObject();

        json.accumulate("descripcion", "string");
        json.accumulate("especie", "Enano");

        json.accumulate("foto", foto.getImagen() );

        json.accumulate("nombre", "Hormonel");
        json.accumulate("raza", "Humana");
        json.accumulate("sexo", "Hombre");


        FileWriter fileWriter = new FileWriter(path+"update_mascota_operation/mascota.json");
        fileWriter.write(json.toString(0));
        fileWriter.close();


        this.mockMvc.perform(put("/ServerRESTAPI/UpdatePet/foo@mail.com/Messi").contentType(MediaType.APPLICATION_JSON).content((read_file(path + "update_mascota_operation/mascota.json"))))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetPet/foo@mail.com?nombreMascota=Hormonel"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "update_mascota_operation/output_update_mascota.json")));
    }


    @Test
    public void updateMascotaPeroNoExisteix() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/UpdatePet/foo@mail.com/Messi").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "update_mascota_operation/input_update_mascota.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    public void deleteMascota() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_mascota_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_mascota_operation/input_crea.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLPetsByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_mascota_operation/output_visualiza1.json")));
        this.mockMvc.perform(delete("/ServerRESTAPI/DeletePet/foo@mail.com").param("nombreMascota","Messi"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLPetsByUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_mascota_operation/output_visualiza2.json")));
    }

    @Test
    public void deleteMascotaANDMascotaDontExists() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/DeletePet/foo@mail.com?nombreMascota=Messi").param("nombreMascota","Messi"))
                .andDo(print()).andExpect(status().isNotFound());
    }
}
