package com.TaskSii.repository;

import com.TaskSii.model.FundraisingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {
    List<FundraisingEvent> findByOwnerProfileId(Long ownerProfileId);
    Optional<FundraisingEvent> findByIdAndOwnerProfileId(Long eventId, Long ownerProfileId);

    boolean existsByIdAndOwnerProfileId(Long eventId, Long ownerProfileId);
}
