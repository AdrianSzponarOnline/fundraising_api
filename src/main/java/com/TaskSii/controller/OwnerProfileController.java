package com.TaskSii.controller;

import com.TaskSii.dto.OwnerDTO;
import com.TaskSii.mapper.OwnerProfileMapper;
import com.TaskSii.model.User;
import com.TaskSii.service.OwnerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner-profile")
public class OwnerProfileController {
    private final OwnerProfileService ownerProfileService;
    private final OwnerProfileMapper ownerProfileMapper;

    @Autowired
    public OwnerProfileController(OwnerProfileService ownerProfileService, OwnerProfileMapper ownerProfileMapper) {
        this.ownerProfileService = ownerProfileService;
        this.ownerProfileMapper = ownerProfileMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<OwnerDTO> getOwnerProfile(@AuthenticationPrincipal User user) {
        OwnerDTO dto = ownerProfileMapper.toDto(ownerProfileService.findById(user.getUser_id()));
        return ResponseEntity.ok(dto);
    }
}
