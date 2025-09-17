package com.TaskSii.repository;

import com.TaskSii.model.CollectionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {
    Optional<CollectionBox> findById(Long id);
    List<CollectionBox> findByVolunteerId(Long volunteerId);
}
