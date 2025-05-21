package com.TaskSii.service;

import com.TaskSii.exception.InvalidOperationException;
import com.TaskSii.model.Currency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;
    private final Currency baseCurrency = Currency.USD;
    private final Map<Currency, BigDecimal> rates = new EnumMap<>(Currency.class);


    public ExchangeRateService(RestTemplate restTemplate,
                               @Value("${exchange.api.url}") String apiUrl,
                               @Value("${exchange.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }


    public void loadRates() {
        try {
            String currencies = Arrays.stream(Currency.values())
                    .map(Enum::name)
                    .filter(code -> !code.equals(baseCurrency.name()))
                    .collect(Collectors.joining(","));

            String url = String.format("%s?access_key=%s&currencies=%s", apiUrl, apiKey, currencies);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !Boolean.TRUE.equals(body.get("success"))) {
                throw new InvalidOperationException("Exchange rate API response was invalid");
            }

            Map<String, Object> quotes = (Map<String, Object>) body.get("quotes");

            for (Map.Entry<String, Object> entry : quotes.entrySet()) {
                String key = entry.getKey();
                String targetCode = key.replace(baseCurrency.name(), "");
                try {
                    Currency targetCurrency = Currency.valueOf(targetCode);
                    BigDecimal rate = new BigDecimal(entry.getValue().toString());
                    rates.put(targetCurrency, rate);
                } catch (IllegalArgumentException ignored) {
                }

            }
            rates.put(baseCurrency, BigDecimal.ONE);
        } catch (Exception e) {
            throw new InvalidOperationException("Exchange rate API response was invalid");
        }
    }

    public BigDecimal getRate(Currency from, Currency to) {
        loadRates();
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }
        if (!rates.containsKey(from) || !rates.containsKey(to)) {
            throw new InvalidOperationException("Unsupported currency " + from + " or " + to);
        }
        BigDecimal usdToFrom = rates.get(from);
        BigDecimal usdToTo = rates.get(to);

        return usdToTo.divide(usdToFrom, 4, RoundingMode.HALF_UP);
    }

}
