package com.TaskSii.config;

import com.TaskSii.model.ERole;
import com.TaskSii.model.Role;
import com.TaskSii.model.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if ("testuser".equals(username)) {
                    User user = new User();
                    user.setUser_id(1L);
                    user.setEmail("test@test.com");
                    user.setPassword("password");
                    user.setEnabled(true);
                    
                    Role ownerRole = new Role();
                    ownerRole.setRole(ERole.ROLE_OWNER);
                    user.setRoles(Set.of(ownerRole));
                    
                    return user;
                }
                throw new UsernameNotFoundException("User not found");
            }
        };
    }
}
