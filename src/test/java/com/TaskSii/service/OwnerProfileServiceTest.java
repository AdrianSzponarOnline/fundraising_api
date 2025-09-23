package com.TaskSii.service;

import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.repository.OwnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerProfileServiceTest {

    private OwnerProfileRepository repo;
    private OwnerProfileService service;

    @BeforeEach
    void setup() {
        repo = mock(OwnerProfileRepository.class);
        service = new OwnerProfileService(repo);
    }

    @Test
    void findById_success() {
        OwnerProfile op = new OwnerProfile();
        when(repo.findById(1L)).thenReturn(Optional.of(op));
        assertEquals(op, service.findById(1L));
    }

    @Test
    void findById_notFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_persists() {
        OwnerProfile op = new OwnerProfile();
        when(repo.save(op)).thenReturn(op);
        assertEquals(op, service.save(op));
    }

    @Test
    void existsById_delegates() {
        when(repo.existsById(1L)).thenReturn(true);
        assertTrue(service.existsById(1L));
    }
}


