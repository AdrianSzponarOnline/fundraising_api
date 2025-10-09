package com.TaskSii.controller;

import com.TaskSii.config.ControllerTestConfig;
import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.dto.collectionbox.AssignVolunteerRequestDTO;
import com.TaskSii.service.VolunteerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VolunteerController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(ControllerTestConfig.class)
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VolunteerService volunteerService;

    @Test
    @WithMockUser(roles = {"OWNER"})
    void listByCurrentOwner_returnsList() throws Exception {
        VolunteerResponseDTO dto = new VolunteerResponseDTO(1L, "A", "B", "e", "p", 5L, List.of());
        Mockito.when(volunteerService.listByOwner(anyLong())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/volunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].ownerProfileId").value(5L));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void getById_returnsVolunteer() throws Exception {
        VolunteerResponseDTO dto = new VolunteerResponseDTO(2L, "A", "B", "e", "p", 5L, List.of());
        Mockito.when(volunteerService.getByIdForOwner(eq(2L), anyLong())).thenReturn(dto);

        mockMvc.perform(get("/api/volunteers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.email").value("e"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void create_createsVolunteer() throws Exception {
        VolunteerResponseDTO dto = new VolunteerResponseDTO(10L, "A", "B", "e", "+1", 5L, List.of());
        Mockito.when(volunteerService.create(any(VolunteerCreateDTO.class), anyLong())).thenReturn(dto);

        String body = "{\"firstName\":\"A\",\"lastName\":\"B\",\"email\":\"e\",\"phoneNumber\":\"+1\",\"password\":\"pass\",\"ownerProfileId\":5}";
        mockMvc.perform(post("/api/volunteers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.ownerProfileId").value(5L));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void update_updatesVolunteer() throws Exception {
        VolunteerResponseDTO dto = new VolunteerResponseDTO(10L, "X", "B", "e", "+1", 5L, List.of());
        Mockito.when(volunteerService.updateForOwner(eq(10L), any(VolunteerUpdateDTO.class), anyLong())).thenReturn(dto);

        String body = "{\"firstName\":\"X\"}";
        mockMvc.perform(put("/api/volunteers/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("X"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void delete_deletesVolunteer() throws Exception {
        Mockito.doNothing().when(volunteerService).deleteForOwner(eq(10L), anyLong());
        mockMvc.perform(delete("/api/volunteers/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void assignVolunteerToBox_assignsVolunteer() throws Exception {
        Mockito.doNothing().when(volunteerService).assignVolunteerToBox(any(AssignVolunteerRequestDTO.class), anyLong());

        String body = "{\"volunteerId\": 1, \"boxId\": 2}";
        mockMvc.perform(post("/api/volunteers/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}


