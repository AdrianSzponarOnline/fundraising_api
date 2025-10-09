package com.TaskSii.controller;

import com.TaskSii.config.ControllerTestConfig;
import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.mapper.OwnerProfileMapper;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.service.OwnerProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Set;

@WebMvcTest(controllers = OwnerProfileController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(ControllerTestConfig.class)
public class OwnerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerProfileService ownerProfileService;

    @MockitoBean
    private OwnerProfileMapper ownerProfileMapper;

    private OwnerProfile ownerProfile;
    private OwnerDTO ownerDTO;

    @BeforeEach
    void setUp() {
        ownerProfile = OwnerProfile.builder()
                .id(1L)
                .organizationName("Test Organization")
                .nip("1234567890")
                .regon("12345678901234")
                .krs("1234567890")
                .phoneNumber("123456789")
                .email("john.doe@example.com")
                .build();

        ownerDTO = new OwnerDTO(1L, "john.doe@example.com", Set.of("ROLE_OWNER"), 
                "Test Organization", "1234567890", "12345678901234", "1234567890", "123456789", List.of());
    }

    @Test
    @WithUserDetails(value = "testuser", userDetailsServiceBeanName = "testUserDetailsService")
    void shouldGetOwnerProfile() throws Exception {
        when(ownerProfileService.findById(anyLong())).thenReturn(ownerProfile);
        when(ownerProfileMapper.toDto(ownerProfile)).thenReturn(ownerDTO);

        mockMvc.perform(get("/api/owner-profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.organizationName").value("Test Organization"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldForbidAccessForNonOwners() throws Exception {
        mockMvc.perform(get("/api/owner-profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/owner-profile"))
                .andExpect(status().isUnauthorized());
    }
}
