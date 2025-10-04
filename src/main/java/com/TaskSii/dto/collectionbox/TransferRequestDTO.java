package com.TaskSii.dto.collectionbox;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequestDTO(
        @NotNull(message = "Box id cannot be null")
        @Positive(message = "Box id must be positive")
        Long boxId
) {
}


