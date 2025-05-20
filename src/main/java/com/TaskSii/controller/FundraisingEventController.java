package com.TaskSii.controller;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.dto.FundraisingEventDTO;
import com.TaskSii.mapper.FundraisingEventMapper;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.service.FundraisingEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class FundraisingEventController {
    private final FundraisingEventService fundraisingEventService;
    private final FundraisingEventMapper fundraisingEventMapper;

    @Autowired
    public FundraisingEventController(FundraisingEventService fundraisingEventService, FundraisingEventMapper fundraisingEventMapper) {
        this.fundraisingEventService = fundraisingEventService;
        this.fundraisingEventMapper = fundraisingEventMapper;
    }

    @PostMapping
    public ResponseEntity<FundraisingEventDTO> createFundraisingEvent(@Valid @RequestBody CreateFundraisingEventDTO fundraisingEventDTO) {
        FundraisingEvent fundraisingEvent = fundraisingEventService.createFundraisingEvent(
                fundraisingEventDTO.getEventName(),
                fundraisingEventDTO.getCurrency());
        FundraisingEventDTO fundraisingEventDTOResponse = fundraisingEventMapper.toDTO(fundraisingEvent);
        return ResponseEntity.ok(fundraisingEventDTOResponse);
    }
}
