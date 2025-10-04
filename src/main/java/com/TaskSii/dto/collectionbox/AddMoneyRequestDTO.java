package com.TaskSii.dto.collectionbox;

import com.TaskSii.model.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AddMoneyRequestDTO(
        @NotNull(message = "Box id cannot be null")
        @Positive(message = "Box id must be positive")
        Long boxId,

        @NotNull(message = "Currency cannot be null")
        Currency currency,

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount
) {
}


