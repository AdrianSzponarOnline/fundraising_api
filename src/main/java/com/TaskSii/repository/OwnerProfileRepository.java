package com.TaskSii.repository;

import com.TaskSii.model.OwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, Long> {
    @Query("SELECT op FROM OwnerProfile op WHERE op.user = :userId")
    Optional<OwnerProfile> findByUserId(@Param("userId") Long userId);
}
