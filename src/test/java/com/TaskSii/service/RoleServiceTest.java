package com.TaskSii.service;

import com.TaskSii.exception.RoleNotFountException;
import com.TaskSii.model.ERole;
import com.TaskSii.model.Role;
import com.TaskSii.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void setup() {
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleService(roleRepository);
    }

    @Test
    void findByRole_success() {
        Role role = new Role();
        role.setRole(ERole.ROLE_USER);
        when(roleRepository.findByRole(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        Role found = roleService.findByRole(ERole.ROLE_USER);
        assertEquals(ERole.ROLE_USER, found.getRole());
    }

    @Test
    void findByRole_notFound_throws() {
        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        assertThrows(RoleNotFountException.class, () -> roleService.findByRole(ERole.ROLE_ADMIN));
    }
}


