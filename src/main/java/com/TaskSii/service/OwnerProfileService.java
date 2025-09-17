package com.TaskSii.service;

import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.repository.OwnerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerProfileService {
    OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public OwnerProfileService(OwnerProfileRepository ownerProfileRepository) {
        this.ownerProfileRepository = ownerProfileRepository;
    }

    public OwnerProfile findById(Long id) {
        return ownerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OwnerProfile with id " + id + " not found" ));

    }
    public OwnerProfile save(OwnerProfile ownerProfile) {
        return ownerProfileRepository.save(ownerProfile);
    }

    public boolean existsById(Long id) {
        return ownerProfileRepository.existsById(id);
    }

}
