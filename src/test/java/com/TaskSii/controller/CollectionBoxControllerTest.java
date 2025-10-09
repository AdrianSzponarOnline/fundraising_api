package com.TaskSii.controller;

import com.TaskSii.config.ControllerTestConfig;
import com.TaskSii.dto.collectionbox.CollectionBoxDTO;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.Currency;
import com.TaskSii.model.User;
import com.TaskSii.service.CollectionBoxService;
import com.TaskSii.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CollectionBoxController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(ControllerTestConfig.class)
public class CollectionBoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @MockitoBean
    private CollectionBoxMapper collectionBoxMapper;

    private CollectionBox collectionBox;
    private CollectionBoxDTO collectionBoxDTO;

    @BeforeEach
    void setUp() {
        collectionBox = new CollectionBox();
        collectionBox.setId(1L);

        collectionBoxDTO = new CollectionBoxDTO(1L, true, false, null, null, null, null, null, null, null);
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldRegisterBox() throws Exception {
        when(collectionBoxService.registerBox(anyLong(), anyLong())).thenReturn(collectionBox);
        when(collectionBoxMapper.toDTO(collectionBox)).thenReturn(collectionBoxDTO);

        mockMvc.perform(post("/api/boxes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.assigned").value(false));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetAllBoxes() throws Exception {
        List<CollectionBox> boxes = Arrays.asList(collectionBox);
        List<CollectionBoxDTO> boxDTOs = Arrays.asList(collectionBoxDTO);

        when(collectionBoxService.getAllBoxesForOwner(anyLong())).thenReturn(boxes);
        when(collectionBoxMapper.toDTO(boxes)).thenReturn(boxDTOs);

        mockMvc.perform(get("/api/boxes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].empty").value(true))
                .andExpect(jsonPath("$[0].assigned").value(false));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetBoxById() throws Exception {
        when(collectionBoxService.getBoxByIdForOwner(eq(1L), anyLong())).thenReturn(collectionBox);
        when(collectionBoxMapper.toDTO(collectionBox)).thenReturn(collectionBoxDTO);

        mockMvc.perform(get("/api/boxes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.assigned").value(false));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldDeleteBox() throws Exception {
        doNothing().when(collectionBoxService).deleteBoxForOwner(eq(1L), anyLong());

        mockMvc.perform(delete("/api/boxes?id=1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldAssignBoxToEvent() throws Exception {
        String jsonBody = "{\"boxId\": 1, \"eventId\": 2}";
        
        CollectionBox assignedBox = new CollectionBox();
        assignedBox.setId(1L);
        CollectionBoxDTO assignedBoxDTO = new CollectionBoxDTO(1L, true, true, null, null, 2L, "Test Event", null, null, null);

        when(collectionBoxService.assignBoxToEventForOwner(eq(1L), eq(2L), anyLong())).thenReturn(assignedBox);
        when(collectionBoxMapper.toDTO(assignedBox)).thenReturn(assignedBoxDTO);

        mockMvc.perform(put("/api/boxes/assign")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.assigned").value(true));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldAddMoneyToBox() throws Exception {
        String jsonBody = "{\"boxId\": 1, \"currency\": \"PLN\", \"amount\": 100.0}";

        doNothing().when(collectionBoxService).addMoney(
                eq(1L),
                eq(Currency.PLN),
                eq(new BigDecimal("100.0"))
        );

        mockMvc.perform(post("/api/boxes/add-money")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldTransferMoney() throws Exception {
        doNothing().when(collectionBoxService).transferMoneyToEventForOwner(eq(1L), anyLong());

        mockMvc.perform(post("/api/boxes/transfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"boxId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldForbidAccessForNonOwners() throws Exception {
        mockMvc.perform(post("/api/boxes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\": 2}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(post("/api/boxes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventId\": 2}"))
                .andExpect(status().isUnauthorized());
    }
}