package com.TaskSii.service;

import com.TaskSii.dto.*;
import com.TaskSii.exception.EmailAlreadyExistsException;
import com.TaskSii.exception.RoleNotFountException;
import com.TaskSii.mapper.AddressMapper;
import com.TaskSii.mapper.OwnerProfileMapper;
import com.TaskSii.model.*;
import com.TaskSii.repository.OwnerProfileRepository;
import com.TaskSii.repository.RoleRepository;
import com.TaskSii.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerProfileMapper ownerProfileMapper;
    private final AddressMapper addressMapper;
    private final OwnerProfileRepository ownerProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User details not found for the user " + username));

        Set<GrantedAuthority> grantedAuthorities = user.getRoles().stream().map(
                authority -> new SimpleGrantedAuthority(authority.getRole().name())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    @Transactional
    public UserDto createUser(RegisterRequestDTO request) {
        checkEmail(request.email());
        Role role = roleRepository.findByRole(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFountException("ROLE_USER not found"));
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Transactional
    public OwnerDTO createOwner(RegisterOwnerDTO dto){
        checkEmail(dto.email());

        Role ownerRole = roleRepository.findByRole(ERole.ROLE_ADMIN)
                .orElseThrow(()-> new RoleNotFountException("ROLE_OWNER not found"));

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(ownerRole));
        User savedUser = userRepository.save(user);

        OwnerProfile profile = ownerProfileMapper.fromRegisterOwnerDTO(dto);
        profile.setUser(user);

        if (dto.addresses() != null) {
            for (AddressDTO addressDTO : dto.addresses()) {
                Address address = addressMapper.fromAddressDTO(addressDTO);
                profile.addAddress(address);
            }
        }
        OwnerProfile savedProfile = ownerProfileRepository.save(profile);
        return ownerProfileMapper.toDto(savedProfile);
    }

    private Set<Role> resolveRoles(){
        return Set.of(
                roleRepository.findByRole(ERole.ROLE_USER)
                        .orElseThrow(() -> new RoleNotFountException("ROLE_USER not found"))
        );
    }


    private UserDto mapToDto(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();

        return new UserDto(user.getEmail());
    }

    private void checkEmail(String email) throws EmailAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }
    }
    public User findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
        return user;
    }
    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
        return user;
    }

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleRepository roleRepository, OwnerProfileMapper ownerProfileMapper, AddressMapper addressMapper, OwnerProfileRepository ownerProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.ownerProfileMapper = ownerProfileMapper;
        this.addressMapper = addressMapper;
        this.ownerProfileRepository = ownerProfileRepository;
    }
}
