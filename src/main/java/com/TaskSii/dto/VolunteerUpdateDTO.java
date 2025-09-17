package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VolunteerUpdateDTO(
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "First name contains invalid characters")
        String firstName,

        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Last name contains invalid characters")
        String lastName,

        @Email(message = "Email should be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number should be valid")
        @Size(max = 15, message = "Phone number cannot exceed 15 characters")
        String phoneNumber,

        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password) {
}
