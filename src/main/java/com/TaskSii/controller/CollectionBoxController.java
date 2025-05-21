package com.TaskSii.controller;

import com.TaskSii.dto.AddMoneyDTO;
import com.TaskSii.dto.AssignBoxDTO;
import com.TaskSii.dto.CollectionBoxDTO;
import com.TaskSii.mapper.CollectionBoxMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.service.CollectionBoxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable Long id){
        collectionBoxService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("{id}/assign")
    public ResponseEntity<Void> assignToEvent(@PathVariable Long id,@Valid @RequestBody AssignBoxDTO dto){
        collectionBoxService.assignBoxToEvent(id, dto.getEventId());
        return ResponseEntity.ok().build();
    }
    @PostMapping("{id}/add-money")
    public ResponseEntity<Void> addMoney(@PathVariable Long id, @Valid @RequestBody AddMoneyDTO dto){
        collectionBoxService.addMoney(id, dto.getCurrency(), dto.getAmount());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> transfer(@PathVariable Long id) {
        collectionBoxService.transferMoneyToEvent(id);
        return ResponseEntity.ok().build();
    }

}
