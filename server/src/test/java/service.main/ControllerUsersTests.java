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
    public void setMockMvc() throws Exception {
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
    Friends
     */


    @Test
    public void sendFriendRequest_AND_getFriendsRequests() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"send_friend_request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"send_friend_request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"send_friend_request_operation/output_GetUserFriendsRequests_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"send_friend_request_operation/output_GetUserFriendsRequests_USER2.json")));
    }

    @Test
    public void sendFriendRequest_AND_ALREADY_SENT_FRIEND_REQUEST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"send_friend_request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"send_friend_request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void sendFriendRequest_AND_USERS_ALREADY_ARE_FRIENDS() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void sendFriendRequest_AND_USERT_DONT_EXIST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void acceptFriendRequest_AND_getFriends() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"accept_Friend_Request_operation/output_GetUserFriendsRequests_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"accept_Friend_Request_operation/output_GetUserFriendsRequests_USER2.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"accept_Friend_Request_operation/output_GetUserFriends_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"accept_Friend_Request_operation/output_GetUserFriends_USER2.json")));
    }

    @Test
    public void acceptFriendRequest_AND_HAVENT_SENT_FRIEND_REQUEST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void acceptFriendRequest_AND_USERS_ALREADY_ARE_FRIENDS() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"accept_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void acceptFriendRequest_AND_USERT_DONT_EXIST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isNotFound());
    }




}
