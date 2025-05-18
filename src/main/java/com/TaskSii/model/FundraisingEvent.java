package com.TaskSii.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fundraising_event")
public class FundraisingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal accountBalance = BigDecimal.ZERO;

    public FundraisingEvent() {
    }

    public FundraisingEvent(String name, Currency currency) {
        this.name = name;
        this.currency = currency;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    //helper methods

}
