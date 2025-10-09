package com.TaskSii.service;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository, OwnerProfileRepository ownerProfileRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.ownerProfileRepository = ownerProfileRepository;
    }

    public FundraisingEvent createFundraisingEvent(CreateFundraisingEventDTO dto, Long userId) {
        OwnerProfile owner = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"));

        FundraisingEvent event = FundraisingEvent.builder()
                .name(dto.eventName())
                .currency(dto.currency())
                .build();
        owner.addFundraisingEvent(event);

        return fundraisingEventRepository.save(event);
    }
    public FundraisingEvent getFundraisingEventByIdAndOwnerId(Long eventId, Long ownerId) {
        FundraisingEvent event = fundraisingEventRepository.findByIdAndOwnerProfileIdWithBoxes(eventId, ownerId).orElseThrow(() -> new ResourceNotFoundException("Fundraising event not found for id " + eventId));
        return event;
    }

    public List<FundraisingEvent> getAllEventsForOwner(Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        return fundraisingEventRepository.findByOwnerProfileId(ownerProfileId);
    }

    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventRepository.findAll();
    }
}
