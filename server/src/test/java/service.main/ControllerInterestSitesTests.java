package service.main;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerInterestSitesTests extends ControllerIntegrationTests {

    private String path = "../testing_files/server/interest_sites/";

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
}
