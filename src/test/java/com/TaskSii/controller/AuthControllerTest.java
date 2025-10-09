package com.TaskSii.controller;

import com.TaskSii.config.JwtAuthFilter;
import com.TaskSii.config.ControllerTestConfig;
import com.TaskSii.dto.auth.AuthRequestDTO;
import com.TaskSii.dto.auth.AuthResponseDTO;
import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.service.AuthService;
import com.TaskSii.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@Import(ControllerTestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void login_returnsToken() throws Exception {
        when(authService.authenticate(any(AuthRequestDTO.class))).thenReturn(new AuthResponseDTO("tok", "u@e.com", List.of("ROLE_USER")));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"u@e.com\",\"password\":\"Password1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    void register_createsUser() throws Exception {
        when(userService.createUser(any(RegisterRequestDTO.class))).thenReturn(new UserDto("u@e.com"));
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"u@e.com\",\"password\":\"Password1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("u@e.com"));
    }

    // TODO: Fix Authentication parameter injection issue
    // @Test
    // @WithMockUser(username = "test@test.com")
    // void getCurrentUser_returnsCurrentUser() throws Exception {
    //     mockMvc.perform(get("/api/auth/me"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.email").value("test@test.com"));
    // }

    @Test
    void registerOwner_createsOwner() throws Exception {
        when(userService.createOwner(any(RegisterOwnerDTO.class))).thenReturn(new OwnerDTO(1L, "test@test.com", Set.of("ROLE_OWNER"), "Test Organization", "1234567890", "123456789", "1234567890", "123456789", List.of()));
        mockMvc.perform(post("/api/auth/register/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"password\":\"Password1\",\"organizationName\":\"Test Organization\",\"nip\":\"1234567890\",\"regon\":\"123456789\",\"krs\":\"1234567890\",\"phoneNumber\":\"123456789\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.organizationName").value("Test Organization"));
    }
}


