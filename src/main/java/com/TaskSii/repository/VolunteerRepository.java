package com.TaskSii.repository;

import com.TaskSii.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
        List<Volunteer> findAllByOwnerProfileId(Long ownerProfileId);
        boolean existsByIdAndOwnerProfileId(Long volunteerId, Long ownerProfileId);

}
