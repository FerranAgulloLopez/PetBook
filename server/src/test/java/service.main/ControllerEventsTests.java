package service.main;

import org.json.JSONObject;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", password = "test", roles = "USER")
public class ControllerEventsTests extends ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    private String path = "../testing_files/server/events/";

    @Before
    public void ClearDB() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/Test/RemoveDatabase"))
                .andDo(print()).andExpect(status().isOk());
    }

    /*
    Create event
     */

    @Test
    public void createEvent() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_event_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_event_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetAllEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"create_event_operation/output.json")));
    }

    @Test
    public void createEventAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_event_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"create_event_operation/input_event_1.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Get event
     */

    @Test
    public void getEventsByCreator() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_creator/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_creator/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_creator/input_event_1.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_creator/input_event_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_creator/input_event_3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetEventsByCreator").param("mail","user@mail.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_events_by_creator/output.json")));
    }

    @Test
    public void getEventsByCreatorUserDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetEventsByCreator").param("mail","a@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getEventsByParticipant() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_participant/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_participant/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_participant/input_event_1.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_participant/input_event_2.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_by_participant/input_event_3.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetEventsByParticipant").param("mail","b@a.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_events_by_participant/output.json")));
    }

    @Test
    public void getEventsByParticipantUserDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetEventsByParticipant").param("mail","b@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Update event
     */

    @Test
    public void updateEvent() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_event_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_event_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(put("/ServerRESTAPI/events/UpdateEvent").param("eventId",eventId).contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_event_operation/input_update.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetAllEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "update_event_operation/output.json")));
    }

    @Test
    public void updateEventDoesNotExist() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/events/UpdateEvent").param("eventId","4").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_event_operation/input_update.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Add Participant
     */

    @Test
    public void addParticipant() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetAllEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "add_participant_operation/output.json")));
    }

    @Test
    public void addParticipantEventDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId","4").param("participantMail","b@a.com").content(read_file(path+"add_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void addParticipantUserDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void addParticipantAlreadyExists() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }



    /*
    Remove Participant
     */

    @Test
    public void deleteEventParticipant() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(post("/ServerRESTAPI/events/AddEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetAllEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "delete_participant_operation/output.json")));
    }

    @Test
    public void deleteEventParticipantEventDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId","5").param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void deleteEventParticipantUserDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void deleteEventParticipantUserDoesNotParticipateInTheEvent() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_participant_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEventParticipant").contentType(MediaType.APPLICATION_JSON).param("eventId",eventId).param("participantMail","b@a.com"))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    /*
    Delete event
     */

    @Test
    public void deleteEvent() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_event_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        MvcResult result = this.mockMvc.perform(post("/ServerRESTAPI/events/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_event_operation/input_event.json")))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String eventId = aux_getEventId(result);
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEvent").param("eventId",eventId).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/events/GetAllEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "delete_event_operation/output.json")));

    }

    @Test
    public void deleteEventDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/events/DeleteEvent").contentType(MediaType.APPLICATION_JSON).param("eventId","4"))
                .andDo(print()).andExpect(status().isNotFound());
    }


    /*
    Auxiliary operations
     */

    private String aux_getEventId(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        Long id = json.getLong("id");
        return id.toString();
    }

}
