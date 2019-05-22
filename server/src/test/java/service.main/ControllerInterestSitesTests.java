package service.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    Principal principal = new Principal() {
        @Override
        public String getName() {
            return "TEST_PRINCIPAL";
        }
    };

    /*
    Get interest site
     */

    @Test
    public void getAllInterestSites() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_create_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_create_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted", "false"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_interest_site_operation/output_all_1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted", "true"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_interest_site_operation/output_all_2.json")));
    }

    @Test
    public void getInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_create_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_interest_site_operation/input_create_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites/2"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_interest_site_operation/output_one.json")));
    }

    @Test
    public void getInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites/2"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Create interest site
     */

    @Test
    public void createInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted","false"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_interest_site_operation/output.json")));
    }

    @Test
    public void createInterestSiteAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createInterestSiteNotWellFormed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_interest_site_operation/input_create_bad.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Update interest site
     */

    @Test
    public void updateInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(patch("/ServerRESTAPI/interestSites/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_interest_site_operation/input_update.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted","false"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_interest_site_operation/output.json")));
    }

    @Test
    public void updateInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(patch("/ServerRESTAPI/interestSites/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_interest_site_operation/input_update.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void updateInterestSiteNotWellFormed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(patch("/ServerRESTAPI/interestSites/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_interest_site_operation/input_update_bad.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Delete interest site
     */

    @Test
    public void deleteInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/interestSites/1"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted","false"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_interest_site_operation/output.json")));
    }

    @Test
    public void deleteInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/interestSites/1"))
                .andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Vote interest site
     */

    @Test
    public void voteInterestSiteTwoTimes() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"vote_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites/1/vote"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void voteInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites/1/vote"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Unvote interest site
     */

    @Test
    public void unVoteInterestSite() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_interest_site_operation/input_create.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites/1/unVote"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/interestSites").param("accepted","false"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"unvote_interest_site_operation/output.json")));
    }

    @Test
    public void unVoteInterestSiteDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/interestSites/1/unvote"))
                .andDo(print()).andExpect(status().isNotFound());
    }



}
