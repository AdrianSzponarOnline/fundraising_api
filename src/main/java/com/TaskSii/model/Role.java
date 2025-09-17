package com.TaskSii.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleID;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    @Override
    public String getAuthority() {
        return getRole().toString();
    }
}
