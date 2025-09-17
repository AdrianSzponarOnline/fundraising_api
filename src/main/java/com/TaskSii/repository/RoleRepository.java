package com.TaskSii.repository;

import com.TaskSii.model.ERole;
import com.TaskSii.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(ERole name);
}
