package com.TaskSii.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String email,
        List<String> roles) {
}
