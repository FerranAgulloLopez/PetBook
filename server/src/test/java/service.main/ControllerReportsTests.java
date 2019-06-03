package service.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ControllerReportsTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/reports/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
    }

    /*
    Create operation
     */

    @Test
    public void createReport() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_report_operation/output.json")));
    }

    @Test
    public void createReportBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report_bad.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void createReportNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void createReportForbidden() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("d@d.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isForbidden());
    }

    /*
    Vote approved operation
     */

    @Test
    public void voteApproved() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteA_report_operation/output_1.json")));
    }

    @Test
    public void voteApprovedClose() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("d@d.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("e@e.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteA_report_operation/output_2.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/reports/1").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteA_report_operation/output_approved.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/c@c.com").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteA_report_operation/output_user.json")));
    }

    @Test
    public void voteApprovedNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void voteApprovedFOrbidden() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("d@d.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("e@e.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("f@f.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void voteApprovedBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteA_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    /*
    Vote reject operation
     */

    @Test
    public void voteReject() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteR_report_operation/output_1.json")));
    }

    @Test
    public void voteRejectClose() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("d@d.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("e@e.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteR_report_operation/output_2.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/reports/1").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteR_report_operation/output_rejected.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/c@c.com").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"voteR_report_operation/output_user.json")));
    }

    @Test
    public void voteRejectNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void voteRejectBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void voteRejectForbidden() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"voteR_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("d@d.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("e@e.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("j@j.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isForbidden());
    }


    /*
    Unvote operation
     */

    @Test
    public void unVote() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/reports/1/unVote").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"unvote_report_operation/output.json")));
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteReject").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/reports/1/unVote").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/reports").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"unvote_report_operation/output.json")));
    }

    @Test
    public void unVoteNotFound() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/reports/1/unVote").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void unVoteBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/reports/1/unVote").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void unVoteForbidden() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unvote_report_operation/input_report.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("e@e.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/reports/1/voteApprove").with(user("f@f.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/reports/1/unVote").with(user("a@a.com").password("password").roles("ADMIN")))
                .andDo(print()).andExpect(status().isForbidden());
    }



}
