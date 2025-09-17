package com.TaskSii.service;

import com.TaskSii.exception.RoleNotFountException;
import com.TaskSii.model.ERole;
import com.TaskSii.model.Role;
import com.TaskSii.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByRole(ERole role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RoleNotFountException(role.name() + " not found"));
    }

    public Optional<Role> findByRoleOptional(ERole role) {
        return roleRepository.findByRole(role);
    }
}
