package com.TaskSii.controller;

import com.TaskSii.dto.collectionbox.*;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.User;
import com.TaskSii.service.CollectionBoxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {
    private final CollectionBoxService collectionBoxService;
    private final CollectionBoxMapper collectionBoxMapper;

    @Autowired
    public CollectionBoxController(CollectionBoxService collectionBoxService, CollectionBoxMapper collectionBoxMapper) {
        this.collectionBoxService = collectionBoxService;
        this.collectionBoxMapper = collectionBoxMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<CollectionBoxDTO> registerBox(@AuthenticationPrincipal User user, @Valid @RequestBody CreateBoxRequestDTO requestDTO) {
        CollectionBox box = collectionBoxService.registerBox(requestDTO.eventId(), user.getUser_id());
        CollectionBoxDTO responseDto = collectionBoxMapper.toDTO(box);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<CollectionBoxDTO>> getBoxes(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(collectionBoxMapper.toDTO(
                collectionBoxService.getAllBoxesForOwner(currentUser.getUser_id())
        ));
    }
    @GetMapping("/{boxId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<CollectionBoxDTO> getBox(@AuthenticationPrincipal User user, @PathVariable long boxId) {
        CollectionBoxDTO dto = collectionBoxMapper.toDTO(collectionBoxService.getBoxByIdForOwner(boxId, user.getUser_id()));
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteBox(@RequestParam Long id, @AuthenticationPrincipal User currentUser){
        collectionBoxService.deleteBoxForOwner(id, currentUser.getUser_id());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/assign")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<CollectionBoxDTO> assignToEvent(@Valid @RequestBody AssignBoxRequestDTO dto, @AuthenticationPrincipal User currentUser){
        CollectionBoxDTO responseDTO = collectionBoxMapper
                .toDTO(collectionBoxService.assignBoxToEventForOwner(
                        dto.boxId(),
                        dto.eventId(),
                        currentUser.getUser_id()));
        return ResponseEntity.ok(responseDTO);
    }
    @PostMapping("/add-money")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addMoney(@Valid @RequestBody AddMoneyRequestDTO dto){
        collectionBoxService.addMoney(dto.boxId(), dto.currency(), dto.amount());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDTO dto, @AuthenticationPrincipal User currentUser) {
        collectionBoxService.transferMoneyToEventForOwner(dto.boxId(), currentUser.getUser_id());
        return ResponseEntity.ok().build();
    }
}
