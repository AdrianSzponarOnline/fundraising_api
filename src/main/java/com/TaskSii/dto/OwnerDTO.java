package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record OwnerDTO(
        Long id,
        @Email(message = "Email should be valid") String email,
        Set<String> roles,
        @Size(max = 100, message = "Organization name cannot exceed 100 characters")
        String organizationName,
        @Size(max = 10) String nip,
        @Size(max = 14) String regon,
        @Size(max = 10) String krs,
        @Size(max = 15) String phoneNumber,
        List<AddressDTO> addresses
) {
}
