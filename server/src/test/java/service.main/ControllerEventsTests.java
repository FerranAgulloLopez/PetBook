package service.main;

import org.junit.Test;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ControllerEventsTests extends ControllerIntegrationTests {

    private String path = "../testing_files/server/events/";

    /*
    Create event
     */

    @Test
    public void creaYgetALLEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"crea_evento_operation/output_crea_evento.json")));
    }

    @Test
    public void creaEventoPeroJaExisteix() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"crea_evento_operation/input_crea_evento.json")))
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
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_first_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_second_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"get_events_bycreator/input_create_third_event.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getEventsByCreator").param("email","a@a.com"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "get_events_bycreator/output.json")));
    }

    /*
    Update event
     */

    @Test
    public void updateEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"update_evento_operation/output_getAll_evento.json")));
        this.mockMvc.perform(put("/ServerRESTAPI/UpdateEvento/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_update_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "update_evento_operation/output_update_evento.json")));
    }

    @Test
    public void updateEventoPeroNoExisteix() throws Exception {
        this.mockMvc.perform(put("/ServerRESTAPI/UpdateEvento/foo@mail.com").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"update_evento_operation/input_update_evento.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

    /*
    Delete event
     */

    @Test
    public void deleteEvento() throws Exception {
        this.mockMvc.perform(post("/ServerRESTAPI/RegisterUser").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_register.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(post("/ServerRESTAPI/CreaEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_crea_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path+"delete_evento_operation/output_getAll_evento.json")));
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_delete_evento.json")))
                .andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/ServerRESTAPI/getALLEventos"))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(read_file_raw(path + "delete_evento_operation/output_delete_evento.json")));

    }

    @Test
    public void deleteEventoPeroNoExisteix() throws Exception {
        this.mockMvc.perform(delete("/ServerRESTAPI/DeleteEvento").contentType(MediaType.APPLICATION_JSON).content(read_file(path+"delete_evento_operation/input_delete_evento.json")))
                .andDo(print()).andExpect(status().isNotFound());
    }

}
