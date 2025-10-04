package com.TaskSii.dto.collectionbox;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignVolunteerRequestDTO(
        @NotNull(message = "Volunteer id cannot be null")
        @Positive(message = "Volunteer id must be positive")
        Long volunteerId,
        @NotNull(message = "Box id cannot be null")
        @Positive(message = "Box id must be positive")
        Long boxId
) {
}
