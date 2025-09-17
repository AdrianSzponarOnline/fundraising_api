package com.TaskSii.dto;

import com.TaskSii.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FundraisingEventDTO(
        Long id,
        @NotBlank(message = "Event name cannot be blank") String name,
        @NotNull(message = "Currency cannot be null") Currency currency,
        @NotNull(message = "Account balance cannot be null") BigDecimal accountBalance) {
}
