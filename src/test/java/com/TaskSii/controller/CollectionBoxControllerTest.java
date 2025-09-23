package com.TaskSii.controller;


import com.TaskSii.TestSecurityUtils;
import com.TaskSii.dto.CollectionBoxDTO;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.Currency;
import com.TaskSii.service.CollectionBoxService;
import com.TaskSii.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollectionBoxController.class)
public class CollectionBoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @MockitoBean
    private CollectionBoxMapper collectionBoxMapper;

    private final String mockToken = "mock-jwt-token";
    private CollectionBox collectionBox;
    private CollectionBoxDTO collectionBoxDTO;

    @BeforeEach
    void setUp() {

        collectionBox = new CollectionBox();
        collectionBox.setId(1L);

        collectionBoxDTO = new CollectionBoxDTO(1L, true, false);

        TestSecurityUtils.setupJwtMocks(jwtService, "admin", new ArrayList<>(List.of("ROLE_ADMIN")));
    }

    private MockHttpServletRequestBuilder addAuth(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder
                .header("Authorization", "Bearer " + mockToken)
                .with(csrf());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldRegisterBox() throws Exception {
        Mockito.when(collectionBoxService.registerBox()).thenReturn(collectionBox);
        Mockito.when(collectionBoxMapper.toDTO(collectionBox)).thenReturn(collectionBoxDTO);

        mockMvc.perform(addAuth(post("/api/boxes"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.assigned").value(false));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldGetAllBoxes() throws Exception {
        List<CollectionBox> boxes = Arrays.asList(collectionBox);
        List<CollectionBoxDTO> boxDTOs = Arrays.asList(collectionBoxDTO);

        Mockito.when(collectionBoxService.getAllBoxes()).thenReturn(boxes);
        Mockito.when(collectionBoxMapper.toDTO(boxes)).thenReturn(boxDTOs);

        mockMvc.perform(addAuth(get("/api/boxes")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].empty").value(true))
                .andExpect(jsonPath("$[0].assigned").value(false));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldDeleteBox() throws Exception {
        Mockito.doNothing().when(collectionBoxService).deleteBox(1L);

        mockMvc.perform(addAuth(delete("/api/boxes/1")))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldAssignBoxToEvent() throws Exception {
        String jsonBody = "{\"eventId\": 2}";

        Mockito.doNothing().when(collectionBoxService).assignBoxToEvent(1L, 2L);

        mockMvc.perform(addAuth(put("/api/boxes/1/assign"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldAddMoneyToBox() throws Exception {
        String jsonBody = "{\"currency\": \"PLN\", \"amount\": 100.0}";

        Mockito.doNothing().when(collectionBoxService).addMoney(
                Mockito.eq(1L),
                Mockito.eq(Currency.PLN),
                Mockito.eq(new BigDecimal("100.0"))
        );

        mockMvc.perform(addAuth(post("/api/boxes/1/add-money"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"OWNER"})
    void shouldTransferMoney() throws Exception {
        Mockito.doNothing().when(collectionBoxService).transferMoneyToEvent(1L);

        mockMvc.perform(addAuth(post("/api/boxes/1/transfer")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldForbidAccessForNonOwners() throws Exception {
        Mockito.when(jwtService.extractUsername(mockToken)).thenReturn("user");
        Mockito.when(jwtService.extractRoles(mockToken)).thenReturn(new ArrayList<>(List.of("ROLE_USER")));
        Mockito.when(jwtService.isTokenValid(Mockito.eq(mockToken), Mockito.any())).thenReturn(true);

        mockMvc.perform(addAuth(post("/api/boxes"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(post("/api/boxes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}