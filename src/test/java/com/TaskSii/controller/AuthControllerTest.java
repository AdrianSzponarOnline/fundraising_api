package com.TaskSii.controller;

import com.TaskSii.config.JwtAuthFilter;
import com.TaskSii.dto.auth.AuthRequestDTO;
import com.TaskSii.dto.auth.AuthResponseDTO;
import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.service.AuthService;
import com.TaskSii.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
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
}


