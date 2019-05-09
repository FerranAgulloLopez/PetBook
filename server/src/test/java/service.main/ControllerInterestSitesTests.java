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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", password = "test", roles = "USER")
public class ControllerInterestSitesTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/interest_sites/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }

    /*
    Create interest site
     */

    @Test
    public void createInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetInterestSite").param("name","Veterinary").param("localization","10"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_interest_site_operation/output.json")));
    }

    @Test
    public void createInterestSiteAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createInterestSiteUserDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Get interest site
     */

    @Test
    public void getInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetInterestSite").param("name","Veterinary").param("localization","10"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_interest_site_operation/output.json")));
    }

    @Test
    public void getInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRestAPI/GetInterestSite")).andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Vote interest site
     */

    @Test
    public void voteInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetInterestSite").param("name","Veterinary").param("localization","10"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"vote_interest_site_operation/output.json")));
    }

    @Test
    public void voteInterestSiteAccepted() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register4.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register5.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register6.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com3"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com4"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com5"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com6"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetInterestSite").param("name","Veterinary").param("localization","10"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"vote_interest_site_operation/output2.json")));
    }

    @Test
    public void voteInterestSiteTwoTimes() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/SuggestInterestSite").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_suggest.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/VoteInterestSite").param("name","Veterinary").param("localization","10").param("email","petbook@main.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

}
