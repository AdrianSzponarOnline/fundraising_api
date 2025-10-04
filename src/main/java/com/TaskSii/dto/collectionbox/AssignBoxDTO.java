package com.TaskSii.dto.collectionbox;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignBoxDTO(
        @NotNull(message = "Event id cannot be null")
        @Positive(message = "Event id must be positive")
        Long eventId
) {
}
