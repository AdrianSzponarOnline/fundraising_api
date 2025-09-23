package com.TaskSii.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JWTService();
        setField(jwtService, "secretKey", "dGhpc19pc19hX3Zlcnlfc2VjdXJlX2FiY2RlZmdoaWprbG1ub3BxcnM=");
        setField(jwtService, "jwtExpiration", 3600000L);

        Collection<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails = new User("user@example.com", "ENC", authorities);
    }

    @Test
    void generateAndValidateToken_success() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("user@example.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_wrongUser_returnsFalse() {
        String token = jwtService.generateToken(userDetails);
        UserDetails other = new User("other@example.com", "ENC", userDetails.getAuthorities());
        assertFalse(jwtService.isTokenValid(token, other));
    }

    @Test
    void isTokenValid_wrongRoles_returnsFalse() {
        String token = jwtService.generateToken(userDetails);
        UserDetails differentRoles = new User("user@example.com", "ENC", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertFalse(jwtService.isTokenValid(token, differentRoles));
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}


