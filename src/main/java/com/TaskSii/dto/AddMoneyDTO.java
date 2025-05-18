package com.TaskSii.dto;

import com.TaskSii.model.Currency;

import java.math.BigDecimal;

public class AddMoneyDTO {
    private Currency currency;
    private BigDecimal amount;

    public AddMoneyDTO() {}

    public AddMoneyDTO(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
