package com.TaskSii.service;

import com.TaskSii.dto.RegisterRequest;
import com.TaskSii.dto.UserDto;
import com.TaskSii.exception.EmailAlreadyExistsException;
import com.TaskSii.model.ERole;
import com.TaskSii.model.Role;
import com.TaskSii.model.User;
import com.TaskSii.repository.RoleRepository;
import com.TaskSii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User details not found for the user " + username));

        Set<GrantedAuthority> grantedAuthorities = user.getRoles().stream().map(
                authority -> new SimpleGrantedAuthority(authority.getRole().name())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
    public UserDto createUser(RegisterRequest request) throws RoleNotFoundException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email " + request.email() + " is already taken");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword("{bcrypt}"+passwordEncoder.encode(request.password()));
        user.setEnabled(true);

        Set<Role> roles = resolveRoles();
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }
    private Set<Role> resolveRoles() throws RoleNotFoundException {
        return Set.of(
                roleRepository.findByRole(ERole.ROLE_USER)
                        .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"))
        );
    }


    private UserDto mapToDto(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();

        return new UserDto(user.getEmail());
    }

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
}
