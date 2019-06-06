package service.main;

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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerSearchTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/search/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase")
                .with(user("foo@main.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
    }

    /*
        Search User :   searchUsers
     */

    @Test
    public void search_BY_POSTALCODE() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/Search/User?postalCode=12345")
                .with(user("foo@main.com").password("password").roles("USER")))    // Para que lo haga el usuario foo@main.com
                .andDo(print()).andExpect(content().string(read_file_raw(path+"search_user_operation/output_search_BY_POSTALCODE.json")));
    }

    @Test
    public void search_BY_PET_TYPE() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"search_user_operation/input_crea_mascota.json"))
                .with(user("a@.com").password("pass").roles("USER")) )
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/Search/User?petType=Perro")
                .with(user("foo@main.com").password("password").roles("USER")))    // Para que lo haga el usuario foo@main.com
                .andDo(print()).andExpect(content().string(read_file_raw(path+"search_user_operation/output_search_BY_PET_TYPE.json")));

    }

    // 4 usuarios 2 mascotas, buscar por los 3 campos
    @Test
    public void search_BY_PET_TYPE_AND_FIRSTNAME_AND_POSTALCODE() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register4.json")))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"search_user_operation/input_crea_mascota.json"))
                .with(user("a@.com").password("pass").roles("USER")) )
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreatePet").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"search_user_operation/input_crea_mascota3.json"))
                .with(user("a@.com3").password("pass").roles("USER")) )
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/Search/User?petType=Anfield&postalCode=123456789&userName=Goku")
                .with(user("foo@main.com").password("password").roles("USER")))    // Para que lo haga el usuario foo@main.com
                .andDo(print()).andExpect(content().string(read_file_raw(path+"search_user_operation/output_search_BY_PET_TYPE_AND_FIRSTNAME_AND_POSTALCODE.json")));
    }

    @Test
    public void search_BY_FIRSTNAME_AND_POSTALCODE_RETURNS_MORE_THAN_ONE_USER() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "search_user_operation/input_register4.json")))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/Search/User?postalCode=12345&userName=Moha")
                .with(user("foo@main.com").password("password").roles("USER")))    // Para que lo haga el usuario foo@main.com
                .andDo(print()).andExpect(content().string(read_file_raw(path+"search_user_operation/output_search_BY_FIRSTNAME_AND_POSTALCODE.json")));

    }




}
