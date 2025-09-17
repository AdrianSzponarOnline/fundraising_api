package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterOwnerDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @NotBlank(message = "Organization name is required")
        @Size(max = 100, message = "Organization name cannot exceed 100 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Organization name contains invalid characters")
        String organizationName,

        @Size(min = 10, max = 10, message = "NIP must be exactly 10 digits")
        @Pattern(regexp = "^\\d{10}$", message = "NIP must be 10 digits")
        String nip,

        @Pattern(regexp = "^\\d{9}$", message = "REGON must be 9 digits")
        String regon,

        @Pattern(regexp = "^\\d{10}$", message = "KRS must be 10 digits")
        String krs,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number should be valid")
        @Size(max = 15, message = "Phone number cannot exceed 15 characters")
        String phoneNumber,

        List<AddressDTO> addresses
) {
}
