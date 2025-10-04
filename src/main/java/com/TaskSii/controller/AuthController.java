package com.TaskSii.controller;

import com.TaskSii.dto.*;
import com.TaskSii.dto.auth.AuthRequestDTO;
import com.TaskSii.dto.auth.AuthResponseDTO;
import com.TaskSii.service.AuthService;
import com.TaskSii.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(new UserDto(authentication.getName()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/register/owner")
    public ResponseEntity<OwnerDTO> registerOwner(@Valid @RequestBody RegisterOwnerDTO request) {
        OwnerDTO createdOwner = userService.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOwner);
    }
}
