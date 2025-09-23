package com.TaskSii.service;

import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.model.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateServiceTest {

    private RestTemplate restTemplate;
    private ExchangeRateService service;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        service = new ExchangeRateService(restTemplate, "http://api", "key");
    }

    @Test
    void getRate_sameCurrency_returnsOne() {
        BigDecimal rate = service.getRate(Currency.PLN, Currency.PLN);
        assertEquals(0, rate.compareTo(BigDecimal.ONE));
    }

    @Test
    void getRate_supportedPair_usesLoadedRates() {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        Map<String, Object> quotes = new HashMap<>();
        quotes.put("USDPLN", 4.00);
        quotes.put("USDEUR", 0.80);
        body.put("quotes", quotes);
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        BigDecimal ratePlnToEur = service.getRate(Currency.PLN, Currency.EUR);
        // USD->EUR / USD->PLN = 0.80 / 4.00 = 0.20
        assertEquals(0, ratePlnToEur.compareTo(new BigDecimal("0.2000")));
    }

    @Test
    void getRate_unsupportedCurrency_throws() {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        Map<String, Object> quotes = new HashMap<>();
        quotes.put("USDPLN", 4.00);
        body.put("quotes", quotes);
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        assertThrows(InvalidOperationException.class, () -> service.getRate(Currency.EUR, Currency.GBP));
    }

    @Test
    void loadRates_invalidResponse_throws() {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        assertThrows(InvalidOperationException.class, () -> service.getRate(Currency.PLN, Currency.EUR));
    }
}


