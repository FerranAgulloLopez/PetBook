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
import org.springframework.test.web.servlet.MvcResult;

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
@WithMockUser(username = "a@a.com", password = "test", roles = "USER")
public class ControllerUsersTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/users/";

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

    @Test
    public void RegisterUserAgain() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"register_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"register_operation/input_register.json")))
                .andDo(print()).andDo(print()).andExpect(status().isBadRequest());
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
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/ConfirmLogin").param("email", "petbook@main.com").param("password", "believe_on_me")).andReturn();
        String token = getJwtToken(result);
        this.mockMvc.perform(get("/mailConfirmation/"+token))
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
        this.mockMvc.perform(put("/ServerRESTAPI/update/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"updateUserByEmail_operation/input_updateUser.json")))
                .andDo(print()).andExpect(status().isCreated());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUser/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"updateUserByEmail_operation/output_getUser.json")));
    }


    @Test
    public void updateUserByEmailNOTINDB() throws Exception {
        this.mockMvc.perform(post("/ServerRestAPI/update/noUser@petbook.com")).andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Set profile picture
     */

    @Test
    public void setProfilePicture() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"setProfilePicture_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/setPicture/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"setProfilePicture_operation/input_picture.json")))
                .andDo(print()).andExpect(status().isOk());
    }


    /*
    Get profile picture
     */

    @Test
    public void getProfilePicture() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getProfilePicture_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/setPicture/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"getProfilePicture_operation/input_picture.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getPicture/foo@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"getProfilePicture_operation/output_getPicture.json")));
    }


    /*
    Set token firebase
     */

    @Test
    public void setTokenFirebase() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"setToken_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/token/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"setToken_operation/input_token.json")))
                .andDo(print()).andExpect(status().isOk());
    }


    /*
    Get wall posts
     */

    @Test
    public void getUserWallPosts() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/get_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/get_operation/input_post_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/get_operation/input_post_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"wallPosts/get_operation/output.json")));
    }

    /*
    Create wall post
     */

    @Test
    public void createWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/create_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/create_operation/input_post.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"wallPosts/create_operation/output.json")));
    }

    @Test
    public void createWallPostMalformed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/create_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/create_operation/input_post_malformed_1.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/create_operation/input_post_malformed_2.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Update wall post
     */

    @Test
    public void updateWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_post_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_post_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts").param("wallPostId", "2").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_update.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"wallPosts/update_operation/output.json")));
    }

    @Test
    public void updateWallPostNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts").param("wallPostId", "1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_update.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void updateWallPostMalformed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_post_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts").param("wallPostId", "1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_update_1.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts").param("wallPostId", "1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/update_operation/input_update_2.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    /*
    Delete wall post
     */

    @Test
    public void deleteWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/delete_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/delete_operation/input_post.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/users/WallPosts").param("wallPostId", "1"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"wallPosts/delete_operation/output.json")));
    }

    @Test
    public void deleteWallPostNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"wallPosts/delete_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/users/WallPosts").param("wallPostId", "1"))
                .andDo(print()).andExpect(status().isNotFound());
    }




    /*
    Auxiliary operations
     */

    private String getJwtToken(MvcResult result) {
        String token = result.getResponse().getHeader("Authorization");
        return token.replaceAll("Bearer ", "");
    }




}
