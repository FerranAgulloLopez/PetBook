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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerWallPostsTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/users/wallPosts/";


    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase").with(user("foo@main.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
    }

    /*
    Get wall posts
     */

    @Test
    public void getUserWallPosts() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "get_operation/input_post_1.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path + "get_operation/input_post_2.json")).with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_operation/output.json")));
    }

    /*
    Create wall post
     */

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void createWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_operation/input_post.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_operation/output.json")));
    }

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void createWallPostMalformed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_operation/input_post_malformed_1.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_operation/input_post_malformed_2.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Update wall post
     */

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void updateWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_post_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_post_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts/2").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_update.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_operation/output.json")));
    }

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void updateWallPostNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_update.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void updateWallPostMalformed() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_post_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_update_1.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(put("/ServerRESTAPI/users/WallPosts/1").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_operation/input_update_2.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    /*
    Delete wall post
     */

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void deleteWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_post.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/users/WallPosts/1"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_operation/output.json")));
    }

    @Test
    @WithMockUser(username = "a@a.com", password = "test", roles = "USER")
    public void deleteWallPostNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/users/WallPosts/1"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void deleteWallPostWithRetweets() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_register_3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("c@c.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/users/WallPosts/1").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_operation/output_1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/users/b@b.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_operation/output_2.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/users/c@c.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_operation/output_3.json")));
    }

    /*
    Like
     */

    @Test
    public void likeWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"like_operation/output_1.json")));
    }

    @Test
    public void likeWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void likeWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void unlikeWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLike").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"like_operation/output_2.json")));
    }

    @Test
    public void unlikeWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Like").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLike").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void unlikeWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"like_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLike").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLike").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Love
     */

    @Test
    public void loveWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"love_operation/output_1.json")));
    }

    @Test
    public void loveWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void loveWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void unloveWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLove").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"love_operation/output_2.json")));
    }

    @Test
    public void unloveWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Love").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLove").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void unloveWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"love_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLove").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnLove").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Retweet
     */

    @Test
    public void retweetWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"retweet_operation/output_1.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/users/b@b.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"retweet_operation/output_2.json")));
    }

    @Test
    public void retweetWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void retweetWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet_bad.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("a@a.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void unretweetWallPost() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/users/a@a.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"retweet_operation/output_3.json")));
        this.mockMvc.perform(get("/ServerRESTAPI/users/b@b.com/WallPosts").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"retweet_operation/output_4.json")));
    }

    @Test
    public void unretweetWallPostNotFound() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void unretweetWallPostBadRequest() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_register_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/WallPosts").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_post.json")).with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/Retweet").with(user("b@b.com").password("password").roles("USER")).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"retweet_operation/input_retweet.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("a@a.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/users/a@a.com/WallPosts/1/UnRetweet").with(user("b@b.com").password("password").roles("USER")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

}
