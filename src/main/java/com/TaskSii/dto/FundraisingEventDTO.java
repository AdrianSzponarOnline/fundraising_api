package com.TaskSii.dto;

import com.TaskSii.model.Currency;

import java.math.BigDecimal;

public class FundraisingEventDTO {
    Long id;
    private String name;
    private Currency currency;
    private BigDecimal accountBalance;

    public FundraisingEventDTO() {
    }

    public FundraisingEventDTO(Long id, String name, Currency currency, BigDecimal accountBalance) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.accountBalance = accountBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
