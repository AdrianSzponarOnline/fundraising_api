package com.TaskSii.service;

import com.TaskSii.dto.VolunteerCreateDTO;
import com.TaskSii.dto.VolunteerResponseDTO;
import com.TaskSii.dto.VolunteerUpdateDTO;
import com.TaskSii.exception.ResourceNotFoundException;
import com.TaskSii.mapper.VolunteerMapper;
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
    private final OwnerProfileRepository ownerProfileRepository;
    private final VolunteerMapper volunteerMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository,
                            OwnerProfileRepository ownerProfileRepository,
                            VolunteerMapper volunteerMapper,
                            PasswordEncoder passwordEncoder) {
        this.volunteerRepository = volunteerRepository;
        this.ownerProfileRepository = ownerProfileRepository;
        this.volunteerMapper = volunteerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<VolunteerResponseDTO> listByOwner(Long ownerProfileId) {
        return volunteerRepository.findAllByOwnerProfileId(ownerProfileId)
                .stream().map(volunteerMapper::toDto).toList();
    }

    public VolunteerResponseDTO getById(Long volunteerId) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + volunteerId + " not found"));
        return volunteerMapper.toDto(volunteer);
    }

    @Transactional
    public VolunteerResponseDTO create(VolunteerCreateDTO dto) {
        OwnerProfile owner = ownerProfileRepository.findById(dto.ownerProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("OwnerProfile with id " + dto.ownerProfileId() + " not found"));

        Volunteer volunteer = volunteerMapper.toEntity(dto);
        volunteer.setOwnerProfile(owner);
        volunteer.setPassword(passwordEncoder.encode(volunteer.getPassword()));

        Volunteer saved = volunteerRepository.save(volunteer);
        return volunteerMapper.toDto(saved);
    }

    @Transactional
    public VolunteerResponseDTO update(Long volunteerId, VolunteerUpdateDTO dto) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer with id " + volunteerId + " not found"));

        volunteerMapper.updateVolunteerFromDto(dto, volunteer);
        if (dto.password() != null) {
            volunteer.setPassword(passwordEncoder.encode(dto.password()));
        }
        Volunteer saved = volunteerRepository.save(volunteer);
        return volunteerMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long volunteerId, Long ownerProfileId) {
        boolean allowed = volunteerRepository.existsByIdAndOwnerProfileId(volunteerId, ownerProfileId);
        if (!allowed) {
            throw new ResourceNotFoundException("Volunteer not found for owner");
        }
        volunteerRepository.deleteById(volunteerId);
    }
}
