package com.TaskSii.service;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.mapper.VolunteerMapper;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.model.Volunteer;
import com.TaskSii.repository.OwnerProfileRepository;
import com.TaskSii.repository.VolunteerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerServiceTest {

    private VolunteerRepository repo;
    private OwnerProfileRepository ownerRepo;
    private VolunteerMapper mapper;
    private PasswordEncoder encoder;
    private VolunteerService service;

    @BeforeEach
    void setup() {
        repo = mock(VolunteerRepository.class);
        ownerRepo = mock(OwnerProfileRepository.class);
        mapper = mock(VolunteerMapper.class);
        encoder = mock(PasswordEncoder.class);
        service = new VolunteerService(repo, ownerRepo, mapper, encoder);
    }

    @Test
    void listByOwner_mapsResults() {
        Volunteer v = new Volunteer();
        when(repo.findAllByOwnerProfileId(1L)).thenReturn(List.of(v));
        VolunteerResponseDTO dto = new VolunteerResponseDTO(1L, "A", "B", "e", "p", 1L, List.of());
        when(mapper.toDto(v)).thenReturn(dto);
        List<VolunteerResponseDTO> res = service.listByOwner(1L);
        assertEquals(1, res.size());
        assertEquals(1L, res.get(0).id());
    }

    @Test
    void getById_success() {
        Volunteer v = new Volunteer();
        when(repo.findById(1L)).thenReturn(Optional.of(v));
        VolunteerResponseDTO dto = new VolunteerResponseDTO(1L, "A", "B", "e", "p", 1L, List.of());
        when(mapper.toDto(v)).thenReturn(dto);
        assertEquals(1L, service.getById(1L).id());
    }

    @Test
    void getById_notFound_throws() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void create_success_encodesPassword_andSetsOwner() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO("A", "B", "e", "+1", "pass", 5L);
        OwnerProfile owner = new OwnerProfile();
        when(ownerRepo.findById(5L)).thenReturn(Optional.of(owner));

        Volunteer entity = new Volunteer();
        entity.setPassword("pass");
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(encoder.encode("pass")).thenReturn("ENC");
        Volunteer saved = new Volunteer();
        when(repo.save(entity)).thenReturn(saved);
        VolunteerResponseDTO response = new VolunteerResponseDTO(10L, "A", "B", "e", "+1", 5L, List.of());
        when(mapper.toDto(saved)).thenReturn(response);

        VolunteerResponseDTO res = service.create(dto);
        assertEquals("ENC", entity.getPassword());
        assertEquals(5L, res.ownerProfileId());
        assertEquals(owner, entity.getOwnerProfile());
    }

    @Test
    void create_ownerNotFound_throws() {
        VolunteerCreateDTO dto = new VolunteerCreateDTO("A", "B", "e", "+1", "pass", 5L);
        when(ownerRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.create(dto));
    }

    @Test
    void update_success_updatesAndEncodesPassword() {
        VolunteerUpdateDTO dto = new VolunteerUpdateDTO("X", null, null, null, "new");
        Volunteer v = new Volunteer();
        when(repo.findById(1L)).thenReturn(Optional.of(v));
        doAnswer(inv -> { ((Volunteer)inv.getArgument(1)).setFirstName("X"); return null; })
                .when(mapper).updateVolunteerFromDto(eq(dto), any(Volunteer.class));
        when(encoder.encode("new")).thenReturn("ENC");
        when(repo.save(v)).thenReturn(v);
        when(mapper.toDto(v)).thenReturn(new VolunteerResponseDTO(1L, "X", "B", "e", "p", 1L, List.of()));

        VolunteerResponseDTO res = service.update(1L, dto);
        assertEquals("X", res.firstName());
        assertEquals("ENC", v.getPassword());
    }

    @Test
    void update_notFound_throws() {
        VolunteerUpdateDTO dto = new VolunteerUpdateDTO(null, null, null, null, null);
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void delete_checksOwnership() {
        when(repo.existsByIdAndOwnerProfileId(10L, 5L)).thenReturn(true);
        service.delete(10L, 5L);
        verify(repo).deleteById(10L);
    }

    @Test
    void delete_wrongOwner_throws() {
        when(repo.existsByIdAndOwnerProfileId(10L, 5L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(10L, 5L));
        verify(repo, never()).deleteById(anyLong());
    }
}


