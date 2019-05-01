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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerForumTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/forum/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }



    /*
    Get all threads
     */

    @Test
    public void getAllForumThreads() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_all_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_all_operation/input_thread_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_all_operation/input_thread_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_all_operation/input_thread_3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_all_operation/input_thread_4.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllForumThreads"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_all_operation/output.json")));
    }



    /*
    Get one thread
     */

    @Test
    public void getForumThread() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_one_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_one_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetForumThread").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_one_operation/output.json")));
    }

    @Test
    public void getForumThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetForumThread"))
                .andDo(print()).andExpect(status().isBadRequest());
    }



    /*
    Create thread
     */

    @Test
    public void createForumThread() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllForumThreads"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_thread_operation/output.json")));
    }

    @Test
    public void createForumThreadUserDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void createForumThreadAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }



    /*
    Update thread
     */

    @Test
    public void updateForumThread() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_thread_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_thread_operation/input_comment.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/forum/UpdateForumThread").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_thread_operation/input_update.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllForumThreads"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_thread_operation/output.json")));
    }

    @Test
    public void updateForumThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/forum/UpdateForumThread").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_thread_operation/input_update.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }



    /*
    Delete thread
     */

    @Test
    public void deleteForumThread() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_thread_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_thread_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/forum/DeleteForumThread").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllForumThreads"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_thread_operation/output.json")));
    }

    @Test
    public void deleteForumThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/forum/DeleteForumThread").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isNotFound());
    }



    /*
    Get all thread comments
     */

    @Test
    public void getAllThreadComments() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_allComments_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_allComments_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_allComments_operation/input_comment_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_allComments_operation/input_comment_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_allComments_operation/input_comment_3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllThreadComments").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"get_allComments_operation/output.json")));
    }

    @Test
    public void getAllThreadCommentsThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllThreadComments").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isNotFound());
    }



    /*
    Create comment
     */

    @Test
    public void createForumComment() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_comment.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllThreadComments").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_comment_operation/output.json")));
    }

    @Test
    public void createForumCommentThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_comment.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void createForumCommentUserDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_comment_1.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void createForumCommentAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_comment.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_comment_operation/input_comment.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }



    /*
    Update comment
     */

    @Test
    public void updateForumComment() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_comment_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_comment_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/forum/UpdateForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/update_comment.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllThreadComments").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_comment_operation/output.json")));
    }

    @Test
    public void updateForumCommentDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/input_comment_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/forum/UpdateForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/update_comment.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void updateForumCommentThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/forum/UpdateForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_comment_operation/update_comment.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }



    /*
    Delete comment
     */

    @Test
    public void deleteForumComment() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_comment_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_comment_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/forum/DeleteForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/forum/GetAllThreadComments").param("creatorMail","petbook@main.com").param("title","I hate London"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_comment_operation/output.json")));
    }

    @Test
    public void deleteForumCommentDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumThread").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_thread.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/forum/CreateNewForumComment").param("creatorMail","petbook@main.com").param("title","I hate London").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_comment_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/forum/DeleteForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void deleteForumCommentThreadDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_comment_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/forum/DeleteForumComment").param("threadCreatorMail","petbook@main.com").param("threadTitle","I hate London").param("commentCreatorMail","petbook@main.com").param("commentCreationDate","2018-05-01T12:34:14.000Z"))
                .andDo(print()).andExpect(status().isNotFound());
    }






}
