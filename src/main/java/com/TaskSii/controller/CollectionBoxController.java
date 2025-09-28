package com.TaskSii.controller;

import com.TaskSii.dto.AddMoneyRequestDTO;
import com.TaskSii.dto.AssignBoxRequestDTO;
import com.TaskSii.dto.TransferRequestDTO;
import com.TaskSii.dto.CollectionBoxDTO;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.service.CollectionBoxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<CollectionBoxDTO> registerBox() {
        CollectionBox box = collectionBoxService.registerBox();
        CollectionBoxDTO dto = collectionBoxMapper.toDTO(box);
        return ResponseEntity.ok(dto);
    }
    @GetMapping
    public ResponseEntity<List<CollectionBoxDTO>> getBoxes() {
        return ResponseEntity.ok(collectionBoxMapper.toDTO(
                collectionBoxService.getAllBoxes()
        ));
    }
    @DeleteMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteBox(@RequestParam Long id){
        collectionBoxService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/assign")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> assignToEvent(@Valid @RequestBody AssignBoxRequestDTO dto){
        collectionBoxService.assignBoxToEvent(dto.boxId(), dto.eventId());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/add-money")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addMoney(@Valid @RequestBody AddMoneyRequestDTO dto){
        collectionBoxService.addMoney(dto.boxId(), dto.currency(), dto.amount());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDTO dto) {
        collectionBoxService.transferMoneyToEvent(dto.boxId());
        return ResponseEntity.ok().build();
    }
}
