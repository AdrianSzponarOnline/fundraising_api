package com.TaskSii.dto;

import com.TaskSii.model.Currency;

import java.math.BigDecimal;

public class FinancialReportDTO {
    private String eventName;
    private BigDecimal amount;
    private Currency currency;

    public FinancialReportDTO() {
    }

    public FinancialReportDTO(String eventName, Currency currency, BigDecimal amount) {
        this.eventName = eventName;
        this.currency = currency;
        this.amount = amount;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}


