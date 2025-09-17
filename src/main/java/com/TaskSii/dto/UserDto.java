package com.TaskSii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Email contains invalid characters")
        String email) {
}
