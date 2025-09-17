package com.TaskSii.dto;

import com.TaskSii.model.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddMoneyDTO(
        @NotNull(message = "Currency cannot be null")
        Currency currency,
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
