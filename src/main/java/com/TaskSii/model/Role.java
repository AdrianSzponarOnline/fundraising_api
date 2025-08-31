package com.TaskSii.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleID;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    public Role() {
    }

    public Role(ERole role) {
        this.role = role;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getRole().toString();
    }
}
