package com.TaskSii.dto;

import java.util.List;

public record AuthResponseDTO(
        String token,
        String email,
        List<String> roles) {
}
