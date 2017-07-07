package com.engagetech.expenses.integration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.money.CurrencyUnit;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.engagetech.expenses.ExpensesApp;
import com.engagetech.expenses.service.FixerExchangeService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SpringBootApplication
public class MockedApplication extends ExpensesApp {
    public static void main(String[] args) {
        SpringApplication.run(MockedApplication.class, args);
    }

    @Bean
    public FixerExchangeService fixerExchangeService(){
        FixerExchangeService fixerExchangeService = Mockito.mock(FixerExchangeService.class);
        Map<CurrencyUnit, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyUnit.EUR, BigDecimal.valueOf(1.13));
        when(fixerExchangeService.getExchangeRatesForDate(any(), any())).thenReturn(exchangeRates);
        return fixerExchangeService;
    }
}