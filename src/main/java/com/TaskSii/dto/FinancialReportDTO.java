package com.TaskSii.dto;

import com.TaskSii.model.Currency;

import java.math.BigDecimal;

public record FinancialReportDTO(
        String eventName,
        BigDecimal amount,
        Currency currency) {

}


