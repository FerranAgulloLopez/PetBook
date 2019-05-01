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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
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
    public void creaYgetALLEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"crea_evento_operation/output_crea_evento.json")));
    }

    @Test
    public void creaEventoPeroJaExisteix() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    /*
    Get event
     */

    @Test
    public void getEventByCreator() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_first_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_second_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_third_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getEventsByCreator").param("email","a@a.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_events_bycreator/output.json")));
    }

    @Test
    public void getEventByCreatorNOTINDB() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/getEventsByCreator").param("email","a@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getEventByParticipant() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_byparticipant/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_byparticipant/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_byparticipant/input_create_first_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_byparticipant/input_create_second_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_byparticipant/input_create_third_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"get_events_byparticipant/input_add_participant.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getEventsByParticipant").param("email","b@a.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_events_byparticipant/output.json")));
    }

    @Test
    public void getEventByParticipantNOTINDB() throws Exception {
        this.mockMvc.perform(get("/ServerRESTAPI/getEventsByParticipant").param("email","b@a.com"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Update event
     */

    @Test
    public void updateEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_evento_operation/output_getAll_evento.json")));
        this.mockMvc.perform(put("/ServerRESTAPI/UpdateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_update_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "update_evento_operation/output_update_evento.json")));
    }

    @Test
    public void updateEventoPeroNoExisteix() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/UpdateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_update_evento.json")))
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
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"add_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "add_participant_operation/output.json")));
    }

    @Test
    public void addParticipantNOTINDBEVENT() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"add_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void addParticipantNOTINDBUSER() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"add_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void addParticipantREPEATED() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"add_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","a@a.com").content(read_file(path+"add_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }



    /*
    Remove Participant
     */

    @Test
    public void removeParticipant() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/addEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"remove_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "remove_participant_operation/output.json")));

        this.mockMvc.perform(post("/ServerRESTAPI/removeEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"remove_participant_operation/input_remove_participant.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "remove_participant_operation/output2.json")));
    }

    @Test
    public void removeParticipantNOTINDBEVENT() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/removeEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"remove_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void removeParticipantNOTINDBUSER() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/removeEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"remove_participant_operation/input_add_participant.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void removeParticipantUSER_NOT_IN_EVENT() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_first_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_register_second_user.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"remove_participant_operation/input_create_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/removeEventParticipant").contentType(MediaType.APPLICATION_JSON).param("participantemail","b@a.com").content(read_file(path+"remove_participant_operation/input_remove_participant.json")))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    /*
    Delete event
     */

    @Test
    public void deleteEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreateEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_evento_operation/output_getAll_evento.json")));
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_delete_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEvents"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "delete_evento_operation/output_delete_evento.json")));

    }

    @Test
    public void deleteEventoPeroNoExisteix() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteEvent").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_delete_evento.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

}