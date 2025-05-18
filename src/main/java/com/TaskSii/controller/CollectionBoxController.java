package com.TaskSii.controller;

import com.TaskSii.dto.AssignBoxDTO;
import com.TaskSii.dto.CollectionBoxDTO;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.service.CollectionBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class CollectionBoxController {
    private final CollectionBoxService collectionBoxService;

    @Autowired
    public CollectionBoxController(CollectionBoxService collectionBoxService) {
        this.collectionBoxService = collectionBoxService;
    }

    @PostMapping
    public ResponseEntity<CollectionBoxDTO> registerBox() {
        CollectionBox box = collectionBoxService.registerBox();
        CollectionBoxDTO dto = new CollectionBoxDTO(box.getId(), box.isEmpty(), box.getFundraisingEvent() != null);
        return ResponseEntity.ok(dto);
    }
    @GetMapping
    public ResponseEntity<List<CollectionBoxDTO>> getBoxes() {
        return ResponseEntity.ok(collectionBoxService.getAllBoxes());
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable Long id){
        collectionBoxService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("{id}/assign")
    public ResponseEntity<Void> assignToEvent(@PathVariable Long id, @RequestBody AssignBoxDTO dto){
        collectionBoxService.assignBoxToEvent(id, dto.getEventId());
        return ResponseEntity.noContent().build();
    }
}
