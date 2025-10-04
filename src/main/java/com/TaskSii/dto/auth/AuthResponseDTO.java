package com.TaskSii.dto.auth;

import java.util.List;

public record AuthResponseDTO(
        String token,
        String email,
        List<String> roles) {
}
