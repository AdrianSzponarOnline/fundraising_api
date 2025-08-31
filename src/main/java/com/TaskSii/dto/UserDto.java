package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email) {
}
