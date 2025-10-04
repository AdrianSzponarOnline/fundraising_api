package com.TaskSii.service;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.Currency;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {
    private FundraisingEventRepository eventRepo;
    private OwnerProfileRepository ownerProfileRepository;
    private FundraisingEventService service;

    @BeforeEach
    void setup() {
        eventRepo = mock(FundraisingEventRepository.class);
        ownerProfileRepository = mock(OwnerProfileRepository.class);
        service = new FundraisingEventService(eventRepo, ownerProfileRepository);
    }

    @Test
    void createFundraisingEvent_success() {
        OwnerProfile owner = new OwnerProfile();
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(owner));
        when(eventRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FundraisingEvent created = service.createFundraisingEvent(new CreateFundraisingEventDTO("Test", Currency.PLN), 1L);
        assertEquals("Test", created.getName());
        assertEquals(Currency.PLN, created.getCurrency());
        assertEquals(owner, created.getOwnerProfile());
    }

    @Test
    void createFundraisingEvent_ownerNotFound_throws() {
        when(ownerProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.createFundraisingEvent(new CreateFundraisingEventDTO("X", Currency.PLN), 1L));
        verify(eventRepo, never()).save(any());
    }

    @Test
    void getAllEvents_returnsList() {
        when(eventRepo.findAll()).thenReturn(List.of(new FundraisingEvent(), new FundraisingEvent()));
        assertEquals(2, service.getAllEvents().size());
    }
}


