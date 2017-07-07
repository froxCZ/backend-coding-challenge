package com.engagetech.expenses.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.money.CurrencyUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.engagetech.expenses.exception.ExchangeRateNotAvailable;

/**
 * Service class for communication with external currency exchange rate service fixer.io
 */
@Service
public class FixerExchangeService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String API_PATH = "http://api.fixer.io/{date}?base=GBP&symbols={currencies}";

    /**
     * Date format used by fixer.
     */
    private DateTimeFormatter fixerDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Returns map of currencies and their exchange rates to GBP.
     * @param date Date for which the currencies should be retrieved.
     * @param currencies List of currencies to retrieve.
     */
    public Map<CurrencyUnit, BigDecimal> getExchangeRatesForDate(LocalDate date, Collection<CurrencyUnit> currencies) {
        String joinedCurrencies = currencies.stream()
                .map(CurrencyUnit::getCurrencyCode)
                .collect(Collectors.joining(","));
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("date", fixerDateFormatter.format(date));
        uriVariables.put("currencies", joinedCurrencies);
        ResponseEntity<ExchangeRateResponse> response = restTemplate
                .getForEntity(API_PATH, ExchangeRateResponse.class, uriVariables);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody().getRates() == null) {
            logger.error("Could not fetch currencies for date {}", date);
            throw new ExchangeRateNotAvailable();
        }
        ExchangeRateResponse exchanges = response.getBody();
        return exchanges.getRates().entrySet().stream()
                .collect(Collectors.toMap(e -> CurrencyUnit.of(e.getKey()), Map.Entry::getValue));
    }

    /**
     * Value object just for parsing the response.
     */
    private static class ExchangeRateResponse {
        Map<String, BigDecimal> rates;

        public Map<String, BigDecimal> getRates() {
            return rates;
        }

        public void setRates(Map<String, BigDecimal> rates) {
            this.rates = rates;
        }
    }

}
