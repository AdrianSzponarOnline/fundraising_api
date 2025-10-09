package com.TaskSii.controller;

import com.TaskSii.config.ControllerTestConfig;
import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.dto.FinancialReportDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.model.Currency;
import com.TaskSii.mapper.FundraisingEventMapper;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.service.FundraisingEventService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FundraisingEventController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(ControllerTestConfig.class)
public class FundraisingEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FundraisingEventService fundraisingEventService;

    @MockitoBean
    private FundraisingEventMapper fundraisingEventMapper;

    private FundraisingEvent fundraisingEvent;
    private FundraisingEventDTO fundraisingEventDTO;
    private FinancialReportDTO financialReportDTO;

    @BeforeEach
    void setUp() {
        fundraisingEvent = FundraisingEvent.builder()
                .id(1L)
                .name("Test Event")
                .currency(Currency.PLN)
                .accountBalance(new BigDecimal("1000.00"))
                .build();

        fundraisingEventDTO = new FundraisingEventDTO(1L, "Test Event", Currency.PLN, 
                new BigDecimal("1000.00"), List.of());

        financialReportDTO = new FinancialReportDTO("Test Event", new BigDecimal("500.00"), Currency.PLN);
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldCreateFundraisingEvent() throws Exception {
        when(fundraisingEventService.createFundraisingEvent(any(CreateFundraisingEventDTO.class), anyLong()))
                .thenReturn(fundraisingEvent);
        when(fundraisingEventMapper.toDTO(fundraisingEvent)).thenReturn(fundraisingEventDTO);

        String jsonBody = "{\"name\":\"Test Event\",\"currency\":\"PLN\"}";

        mockMvc.perform(post("/api/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Event"))
                .andExpect(jsonPath("$.currency").value("PLN"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetAllFundraisingEvents() throws Exception {
        List<FundraisingEvent> events = Arrays.asList(fundraisingEvent);
        List<FundraisingEventDTO> eventDTOs = Arrays.asList(fundraisingEventDTO);

        when(fundraisingEventService.getAllEventsForOwner(anyLong())).thenReturn(events);
        when(fundraisingEventMapper.toFundraisingEventList(events)).thenReturn(eventDTOs);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Event"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetFundraisingEventById() throws Exception {
        when(fundraisingEventService.getFundraisingEventByIdAndOwnerId(eq(1L), anyLong()))
                .thenReturn(fundraisingEvent);
        when(fundraisingEventMapper.toDTO(fundraisingEvent)).thenReturn(fundraisingEventDTO);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Event"));
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetFinancialReport() throws Exception {
        List<FundraisingEvent> events = Arrays.asList(fundraisingEvent);

        when(fundraisingEventService.getAllEventsForOwner(anyLong())).thenReturn(events);
        when(fundraisingEventMapper.toFinancialReport(fundraisingEvent)).thenReturn(financialReportDTO);

        mockMvc.perform(get("/api/events/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName").value("Test Event"))
                .andExpect(jsonPath("$[0].amount").value(500.00))
                .andExpect(jsonPath("$[0].currency").value("PLN"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldForbidAccessForNonOwners() throws Exception {
        String jsonBody = "{\"name\":\"Test Event\",\"currency\":\"PLN\"}";

        mockMvc.perform(post("/api/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isUnauthorized());
    }
}
