package com.TaskSii.dto;
import com.TaskSii.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFundraisingEventDTO {
    @NotBlank(message = "Event name cannot be blank")
    private String eventName;

    @NotNull(message = "Currency cannot be null")
    private Currency currency;

    public CreateFundraisingEventDTO() {
    }

    public CreateFundraisingEventDTO(String eventName, Currency currency) {
        this.eventName = eventName;
        this.currency = currency;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
