package com.TaskSii.dto;
import com.TaskSii.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateFundraisingEventDTO(
        @NotBlank(message = "Event name cannot be blank")
        @Size(max = 255, message = "Event name cannot exceed 255 characters")
        @Pattern(regexp = "^(?!.*[<>]).*$", message = "Event name contains invalid characters")
        String eventName,
        @NotNull(message = "Currency cannot be null")
        Currency currency
) {

}
