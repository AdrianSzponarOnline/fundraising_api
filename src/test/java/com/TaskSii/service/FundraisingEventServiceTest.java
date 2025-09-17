package com.TaskSii.service;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.model.User;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {
    private FundraisingEventRepository eventRepo;
    private UserService userService;
    private OwnerProfileRepository ownerRepo;
    private FundraisingEventService service;

    @BeforeEach
    void setup() {
        eventRepo = mock(FundraisingEventRepository.class);
        userService = mock(UserService.class);
        ownerRepo = mock(OwnerProfileRepository.class);
        service = new FundraisingEventService(eventRepo, userService, ownerRepo);

        // mock security
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("owner@example.com");
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void createFundraisingEvent_success() {
        User current = new User();
        current.setEmail("owner@example.com");
        current.setUser_id(1L);
        when(userService.findUserByEmail("owner@example.com")).thenReturn(current);
        OwnerProfile owner = new OwnerProfile();
        when(ownerRepo.findById(1L)).thenReturn(Optional.of(owner));
        when(eventRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FundraisingEvent created = service.createFundraisingEvent(new CreateFundraisingEventDTO("Test", Currency.PLN));
        assertEquals("Test", created.getName());
        assertEquals(Currency.PLN, created.getCurrency());
        assertEquals(owner, created.getOwnerProfile());
    }

    @Test
    void getAllEvents_returnsList() {
        when(eventRepo.findAll()).thenReturn(List.of(new FundraisingEvent(), new FundraisingEvent()));
        assertEquals(2, service.getAllEvents().size());
    }
}


