package com.TaskSii.controller;

import com.TaskSii.dto.collectionbox.AssignVolunteerRequestDTO;
import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.model.User;
import com.TaskSii.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<VolunteerResponseDTO>> listByCurrentOwner(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(volunteerService.listByOwner(currentUser.getUser_id()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> getById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(volunteerService.getByIdForOwner(id, currentUser.getUser_id()));
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> create(@Valid @RequestBody VolunteerCreateDTO dto, @AuthenticationPrincipal User currentUser) {
        VolunteerResponseDTO created = volunteerService.create(dto, currentUser.getUser_id());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<VolunteerResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody VolunteerUpdateDTO dto,
                                                       @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(volunteerService.updateForOwner(id, dto, currentUser.getUser_id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        volunteerService.deleteForOwner(id, currentUser.getUser_id());
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/assign")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> assignVolunteerToBox(@Valid @RequestBody AssignVolunteerRequestDTO dto, @AuthenticationPrincipal User currentUser) {
        volunteerService.assignVolunteerToBox(dto, currentUser.getUser_id());
        return ResponseEntity.ok().build();
    }
}


