package com.TaskSii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        Long id,

        @NotBlank(message = "Street name is required")
        @Size(max = 100, message = "Street name cannot exceed 100 characters")
        String streetName,

        @NotBlank(message = "City is required")
        @Size(max = 50, message = "City cannot exceed 50 characters")
        String city,

        @Size(max = 50, message = "State cannot exceed 50 characters")
        String state,

        @NotBlank(message = "Country is required")
        @Size(max = 50, message = "Country cannot exceed 50 characters")
        String country,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code cannot exceed 20 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Postal code contains invalid characters")
        String postalCode
) {

    public AddressDTO(String streetName, String city, String state, String country, String postalCode) {
        this(null, streetName, city, state, country, postalCode);
    }
}