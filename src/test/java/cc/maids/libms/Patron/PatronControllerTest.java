package cc.maids.libms.Patron;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cc.maids.libms.LibmsApplication;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Value("${apiPrefix}")
    private String apiPrefix;

    @Autowired
    ObjectMapper om;

    @Test
    @WithMockUser
    public void PatronController_GetAllPatrons_ReturnAllPatronsAndOk() throws Exception {
        Patron patron1 = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();

        Patron patron2 = Patron.builder()
                .name("name2")
                .contactInformation("contactInformation2")
                .build();

        List<Patron> patronList = List.of(patron1, patron2);

        when(patronService.getAllPatrons()).thenReturn(patronList);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/patrons")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.length()", CoreMatchers.is(patronList.size())))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("name1", "name2")));
    }

    @Test
    @WithMockUser
    public void PatronController_GetAllPatrons_ReturnNoContent() throws Exception {
        List<Patron> patronList = new ArrayList<>();

        when(patronService.getAllPatrons()).thenReturn(patronList);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/patrons")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser
    public void PatronController_getPatronById_ReturnPatronAndOk() throws Exception {
        int patronId = 1;
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();
        Optional<Patron> patronOp = Optional.ofNullable(patron);

        when(patronService.getPatronById(patronId)).thenReturn(patronOp);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(patron.getName())))
                .andExpect(jsonPath("$.contactInformation", CoreMatchers.is(patron.getContactInformation())));
    }

    @Test
    @WithMockUser
    public void PatronController_getPatronByIdNotFound_ReturnNotNotFound() throws Exception {
        int patronId = 1;
        Patron patron = null;
        Optional<Patron> patronOp = Optional.ofNullable(patron);

        when(patronService.getPatronById(patronId)).thenReturn(patronOp);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound()).andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    @WithMockUser
    public void PatronController_AddPatron_ReturnCreatedPatronInBody() throws JsonProcessingException, Exception {
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();
        when(patronService.savePatron(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post(apiPrefix + "/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(patron)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(patron.getName())))
                .andExpect(jsonPath("$.contactInformation", CoreMatchers.is(patron.getContactInformation())));
    }

    @Test
    @WithMockUser
    public void PatronController_UpdatePatron_ReturnUpdatedPatron() throws Exception {
        int patronId = 1;
        Patron patron = Patron.builder().id(1L)
                .name("name1")
                .contactInformation("contactInformation1")
                .build();
        when(patronService.savePatron(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(patronService.getPatronById(patronId)).thenReturn(Optional.of(patron));

        ResultActions response = mockMvc.perform(put(apiPrefix + "/patrons/" + patronId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(patron)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(patron.getName())))
                .andExpect(jsonPath("$.contactInformation", CoreMatchers.is(patron.getContactInformation())));

    }

    @Test
    @WithMockUser
    public void PatronController_UpdatePatron_ReturnNotFound() throws Exception {
        int patronId = 1;
        Patron patron = Patron.builder().id(1L)
                .name("name1")
                .contactInformation("contactInformation1")
                .build();
        when(patronService.savePatron(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(patronService.getPatronById(patronId)).thenReturn(Optional.ofNullable(null));

        ResultActions response = mockMvc.perform(put(apiPrefix + "/patrons/" + patronId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(patron)));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void PatronController_AdminDeletePatronByADMIN_DeletePatronAndReturnOk() throws Exception {
        when(patronService.getPatronById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(Mockito.mock(Patron.class)));
        doNothing().when(patronService).deletePatron(ArgumentMatchers.anyLong());

        ResultActions response = mockMvc.perform(delete(apiPrefix + "/patrons/" + 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void PatronController_AdminDeletePatron_DeletePatronAndNotFound() throws Exception {
        when(patronService.getPatronById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(null));
        doNothing().when(patronService).deletePatron(ArgumentMatchers.anyLong());

        ResultActions response = mockMvc.perform(delete(apiPrefix + "/patrons/" + 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());

    }
}
