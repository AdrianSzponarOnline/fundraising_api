package com.TaskSii.dto;

import java.util.Currency;

public class CreateFundraisingEventDTO {
    private String eventName;
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
