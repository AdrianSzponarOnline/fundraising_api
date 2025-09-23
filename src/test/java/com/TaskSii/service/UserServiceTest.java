package com.TaskSii.service;

import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.exception.EmailAlreadyExistsException;
import com.TaskSii.exception.RoleNotFountException;
import com.TaskSii.mapper.AddressMapper;
import com.TaskSii.mapper.OwnerProfileMapper;
import com.TaskSii.model.*;
import com.TaskSii.repository.OwnerProfileRepository;
import com.TaskSii.repository.RoleRepository;
import com.TaskSii.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private OwnerProfileMapper ownerProfileMapper;
    private AddressMapper addressMapper;
    private OwnerProfileRepository ownerProfileRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        ownerProfileMapper = mock(OwnerProfileMapper.class);
        addressMapper = mock(AddressMapper.class);
        ownerProfileRepository = mock(OwnerProfileRepository.class);
        userService = new UserService(userRepository, passwordEncoder, roleRepository, ownerProfileMapper, addressMapper, ownerProfileRepository);
    }

    @Test
    void createUser_success() {
        RegisterRequestDTO dto = new RegisterRequestDTO("john@example.com", "Password1");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        Role role = new Role();
        role.setRole(ERole.ROLE_USER);
        when(roleRepository.findByRole(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Password1")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto userDto = userService.createUser(dto);

        assertEquals("john@example.com", userDto.email());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("ENC", captor.getValue().getPassword());
        assertTrue(captor.getValue().getRoles().stream().anyMatch(r -> r.getRole() == ERole.ROLE_USER));
    }

    @Test
    void createUser_duplicateEmail_throws() {
        RegisterRequestDTO dto = new RegisterRequestDTO("john@example.com", "Password1");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void createUser_missingRole_throws() {
        RegisterRequestDTO dto = new RegisterRequestDTO("john@example.com", "Password1");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByRole(ERole.ROLE_USER)).thenReturn(Optional.empty());
        assertThrows(RoleNotFountException.class, () -> userService.createUser(dto));
    }

    @Test
    void loadUserByUsername_success() {
        User u = new User();
        u.setEmail("john@example.com");
        u.setPassword("ENC");
        Role r = new Role();
        r.setRole(ERole.ROLE_USER);
        u.setRoles(Set.of(r));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(u));

        var details = userService.loadUserByUsername("john@example.com");
        assertEquals("john@example.com", details.getUsername());
        assertEquals("ENC", details.getPassword());
        assertFalse(details.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_notFound_throws() {
        when(userRepository.findByEmail("x@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("x@example.com"));
    }

    @Test
    void createOwner_success() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO("owner@example.com", "Password1", "OrgName", "1234567890", "123456789", "1234567890", "+48123456789", java.util.List.of());
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        Role role = new Role();
        role.setRole(ERole.ROLE_ADMIN);
        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(dto.password())).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        OwnerProfile mapped = new OwnerProfile();
        when(ownerProfileMapper.fromRegisterOwnerDTO(dto)).thenReturn(mapped);
        when(ownerProfileMapper.toDto(any(OwnerProfile.class))).thenReturn(new com.TaskSii.dto.OwnerDTO(1L, "owner@example.com", java.util.Set.of("ROLE_ADMIN"), "OrgName", "1234567890", "123456789", "1234567890", "+48123456789", java.util.List.of()));
        when(ownerProfileRepository.save(any(OwnerProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        var ownerDto = userService.createOwner(dto);
        assertNotNull(ownerDto);
        verify(ownerProfileRepository).save(mapped);
    }

    @Test
    void createOwner_duplicateEmail_throws() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO("owner@example.com", "Password1", "OrgName", "1234567890", "123456789", "1234567890", "+48123456789", java.util.List.of());
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createOwner(dto));
    }

    @Test
    void createOwner_missingRole_throws() {
        RegisterOwnerDTO dto = new RegisterOwnerDTO("owner@example.com", "Password1", "OrgName", "1234567890", "123456789", "1234567890", "+48123456789", java.util.List.of());
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        assertThrows(RoleNotFountException.class, () -> userService.createOwner(dto));
    }
}


