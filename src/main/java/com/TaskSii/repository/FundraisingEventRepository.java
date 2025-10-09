package com.TaskSii.repository;

import com.TaskSii.model.FundraisingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {
    List<FundraisingEvent> findByOwnerProfileId(Long ownerProfileId);
    Optional<FundraisingEvent> findByIdAndOwnerProfileId(Long eventId, Long ownerProfileId);

    @Query("SELECT e FROM FundraisingEvent e " +
            "LEFT JOIN FETCH e.collectionBoxes " +
            "WHERE e.id = :eventId AND e.ownerProfile.id = :ownerId")
    Optional<FundraisingEvent> findByIdAndOwnerProfileIdWithBoxes(Long eventId, Long ownerId);

    boolean existsByIdAndOwnerProfileId(Long eventId, Long ownerProfileId);
}
