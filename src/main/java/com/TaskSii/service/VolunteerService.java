package com.TaskSii.service;

import com.TaskSii.dto.collectionbox.AssignVolunteerRequestDTO;
import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.mapper.VolunteerMapper;
import com.TaskSii.model.CollectionBox;
import com.TaskSii.model.OwnerProfile;
import com.TaskSii.model.Volunteer;
import com.TaskSii.repository.OwnerProfileRepository;
import com.TaskSii.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;
    private final PasswordEncoder passwordEncoder;
    private final CollectionBoxService collectionBoxService;
    private final OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository,
                            VolunteerMapper volunteerMapper,
                            PasswordEncoder passwordEncoder,
                            CollectionBoxService collectionBoxService,
                            OwnerProfileRepository ownerProfileRepository) {
        this.volunteerRepository = volunteerRepository;
        this.volunteerMapper = volunteerMapper;
        this.passwordEncoder = passwordEncoder;
        this.collectionBoxService = collectionBoxService;
        this.ownerProfileRepository = ownerProfileRepository;
    }

    public List<VolunteerResponseDTO> listByOwner(Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        return volunteerRepository.findAllByOwnerProfileId(ownerProfileId)
                .stream().map(volunteerMapper::toDto).toList();
    }

    public VolunteerResponseDTO getByIdForOwner(Long volunteerId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        Volunteer volunteer = volunteerRepository.findByIdAndOwnerProfileId(volunteerId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + volunteerId + " not found or access denied"));
        return volunteerMapper.toDto(volunteer);
    }

    public VolunteerResponseDTO getById(Long volunteerId) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + volunteerId + " not found"));
        return volunteerMapper.toDto(volunteer);
    }


    @Transactional
    public VolunteerResponseDTO create(VolunteerCreateDTO dto, Long userId) {
        OwnerProfile owner = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"));

        Volunteer volunteer = volunteerMapper.toEntity(dto);
        volunteer.setOwnerProfile(owner);
        volunteer.setPassword(passwordEncoder.encode(volunteer.getPassword()));

        Volunteer saved = volunteerRepository.save(volunteer);
        return volunteerMapper.toDto(saved);
    }

    @Transactional
    public VolunteerResponseDTO updateForOwner(Long volunteerId, VolunteerUpdateDTO dto, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        Volunteer volunteer = volunteerRepository.findByIdAndOwnerProfileId(volunteerId, ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + volunteerId + " not found or access denied"));

        volunteerMapper.updateVolunteerFromDto(dto, volunteer);
        if (dto.password() != null) {
            volunteer.setPassword(passwordEncoder.encode(dto.password()));
        }
        Volunteer saved = volunteerRepository.save(volunteer);
        return volunteerMapper.toDto(saved);
    }


    @Transactional
    public void deleteForOwner(Long volunteerId, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        boolean allowed = volunteerRepository.existsByIdAndOwnerProfileId(volunteerId, ownerProfileId);
        if (!allowed) {
            throw new ResourceNotFoundException("Volunteer not found or access denied");
        }
        volunteerRepository.deleteById(volunteerId);
    }

    @Transactional
    public void assignVolunteerToBox(AssignVolunteerRequestDTO dto, Long userId) {
        Long ownerProfileId = ownerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found for user"))
                .getId();
        
        Volunteer volunteer = volunteerRepository.findByIdAndOwnerProfileId(dto.volunteerId(), ownerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + dto.volunteerId() + " not found or access denied"));
        
        CollectionBox box = collectionBoxService.getBoxByIdForOwner(dto.boxId(), userId);
        box.setVolunteer(volunteer);
    }
}
