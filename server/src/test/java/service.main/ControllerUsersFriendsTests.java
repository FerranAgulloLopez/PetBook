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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerUsersFriendsTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/friends/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }


    /*
    Friends send Friend Request
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

    /*
    Friends :    acceptFriendRequest
     */

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

    /*
    Friends :    denyFriendRequest
     */

    @Test
    public void denyFriendRequest_AND_getFriends() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"deny_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"deny_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/denyFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"deny_Friend_Request_operation/output_GetUserFriendsRequests_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriendsRequests/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"deny_Friend_Request_operation/output_GetUserFriendsRequests_USER2.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"deny_Friend_Request_operation/output_GetUserFriends_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"deny_Friend_Request_operation/output_GetUserFriends_USER2.json")));
    }

    @Test
    public void denyFriendRequest_AND_HAVENT_SENT_FRIEND_REQUEST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"deny_Friend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"deny_Friend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/ServerRESTAPI/denyFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    public void denyFriendRequest_AND_USERT_DONT_EXIST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/denyFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Friends :    unfriend
     */

    @Test
    public void  unfriend_AND_getFriends() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unfriend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unfriend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/sendFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(post("/ServerRESTAPI/Unfriend/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isOk());

        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"unfriend_Request_operation/output_GetUserFriends_USER1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/GetUserFriends/foo@main.com2"))
                .andDo(print()).andExpect(content().string(read_file_raw(path+"unfriend_Request_operation/output_GetUserFriends_USER2.json")));
    }


    @Test
    public void unfriend_AND_USERS_ARE_NOT_FRIENDS() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unfriend_Request_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"unfriend_Request_operation/input_register2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/Unfriend/foo@main.com2/foo@main.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void unfriend_AND_USERT_DONT_EXIST() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/acceptFriendRequest/foo@main.com/foo@main.com2"))
                .andDo(print()).andExpect(status().isNotFound());
    }



}
