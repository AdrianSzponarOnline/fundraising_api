package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VolunteerCreateDTO(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "First name contains invalid characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Last name contains invalid characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number should be valid")
        @Size(max = 15, message = "Phone number cannot exceed 15 characters")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @NotNull(message = "Owner profile ID is required")
        @Positive(message = "Owner profile ID must be positive")
        Long ownerProfileId
) {
}
