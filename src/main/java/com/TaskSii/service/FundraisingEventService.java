package com.TaskSii.service;

import com.TaskSii.dto.CreateFundraisingEventDTO;
import com.TaskSii.model.FundraisingEvent;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.model.User;
import com.TaskSii.repository.FundraisingEventRepository;
import com.TaskSii.repository.OwnerProfileRepository;
import com.TaskSii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final UserService userService;
    private final OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public FundraisingEventService(FundraisingEventRepository fundraisingEventRepository, UserService userService, OwnerProfileRepository ownerProfileRepository) {
        this.fundraisingEventRepository = fundraisingEventRepository;
        this.userService = userService;
        this.ownerProfileRepository = ownerProfileRepository;
    }

    public FundraisingEvent createFundraisingEvent(CreateFundraisingEventDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findUserByEmail(email);

        OwnerProfile owner = ownerProfileRepository.findById(currentUser.getUser_id())
                .orElseThrow(() -> new RuntimeException("Owner profile not found for current user"));

        FundraisingEvent event = FundraisingEvent.builder()
                .name(dto.eventName())
                .currency(dto.currency())
                .build();
        owner.addFundraisingEvent(event);

        return fundraisingEventRepository.save(event);
    }
    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventRepository.findAll();
    }
}
