package com.TaskSii.controller;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    @Autowired
    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<VolunteerResponseDTO>> listByOwner(@RequestParam("ownerId") Long ownerId) {
        return ResponseEntity.ok(volunteerService.listByOwner(ownerId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> create(@Valid @RequestBody VolunteerCreateDTO dto) {
        VolunteerResponseDTO created = volunteerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody VolunteerUpdateDTO dto) {
        return ResponseEntity.ok(volunteerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam("ownerId") Long ownerId) {
        volunteerService.delete(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}


