package com.TaskSii.repository;

import com.TaskSii.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
        List<Volunteer> findAllByOwnerProfileId(Long ownerProfileId);
        boolean existsByIdAndOwnerProfileId(Long volunteerId, Long ownerProfileId);
        Optional<Volunteer> findByIdAndOwnerProfileId(Long volunteerId, Long ownerProfileId);
}
