package com.TaskSii.service;

import com.TaskSii.dto.auth.AuthRequestDTO;
import com.TaskSii.dto.auth.AuthResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private AuthService authService;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JWTService.class);
        authService = new AuthService(authenticationManager, jwtService);
    }

    @Test
    void authenticate_success_returnsTokenAndRoles() {
        AuthRequestDTO request = new AuthRequestDTO("user@example.com", "Password1");
        UserDetails userDetails = new User("user@example.com", "ENC", Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(userDetails)).thenReturn("token123");

        AuthResponseDTO response = authService.authenticate(request);

        assertEquals("token123", response.token());
        assertEquals("user@example.com", response.email());
        assertEquals(List.of("ROLE_USER"), response.roles());
    }

    @Test
    void authenticate_badCredentials_throws() {
        AuthRequestDTO request = new AuthRequestDTO("user@example.com", "bad");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(request));
    }

    @Test
    void authenticate_nullRequest_throws() {
        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(null));
    }
}


