package com.engagetech.expenses.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engagetech.expenses.exception.ExchangeRateNotAvailable;
import com.google.common.collect.Sets;

@Service
public class ExchangeService {
    @Autowired
    private FixerExchangeService fixerExchangeService;

    private ConcurrentHashMap<LocalDate, Map<CurrencyUnit, BigDecimal>> currencyCache = new ConcurrentHashMap<>();

    public static final Set<CurrencyUnit> SUPPORTED_CURRENCIES = Sets.newHashSet(CurrencyUnit.EUR, CurrencyUnit.GBP);

    public boolean isCurrencySupported(String currencyCode) {
        try {
            return SUPPORTED_CURRENCIES.contains(CurrencyUnit.of(currencyCode));
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Converts money object to GBP by using exchange rate of provided date. Due to caching and different timezones
     * the rate might differ slightly.
     * Furthermore, since users provide date without timezone, it might happen that the user will be in timezone ahead
     * of local timezone and thus have tomorrow already. Rather than not allowing him to save expense, it seems
     * better to use today's exchange rate. Therefore, we allow "tomorrow dates", but convert them with today's rate.
     */
    public CurrencyConversion convertToGBP(Money money, LocalDate date) {

        if (date.isAfter(tomorrow())) {
            throw new IllegalArgumentException("Future date is not allowed.");
        }
        if (date.isAfter(today())) {
            date = today();
        }

        BigDecimal rate = getExchangeRate(money.getCurrencyUnit(), date);
        BigDecimal convertedAmount = money.getAmount().divide(rate, BigDecimal.ROUND_HALF_EVEN);

        return new CurrencyConversion(rate, convertedAmount);
    }

    private static LocalDate today() {
        return LocalDate.now();
    }

    private static LocalDate tomorrow() {
        return today().plusDays(1);
    }

    /**
     * Returns exchange rate for currency and date. If exchange rate is not in cache it will trigger request to
     * external currency service and cache the results.
     */
    private BigDecimal getExchangeRate(CurrencyUnit currencyUnit, LocalDate date) {
        Map<CurrencyUnit, BigDecimal> dateMap = currencyCache
                .computeIfAbsent(date, key -> fixerExchangeService.getExchangeRatesForDate(date, SUPPORTED_CURRENCIES));
        BigDecimal exchangeRate = dateMap.getOrDefault(currencyUnit, null);
        if (exchangeRate == null) {
            throw new ExchangeRateNotAvailable();
        }
        return exchangeRate;
    }

    public static class CurrencyConversion {
        private BigDecimal exchangeRate;
        private BigDecimal convertedAmount;

        public CurrencyConversion(BigDecimal exchangeRate, BigDecimal convertedAmount) {
            this.exchangeRate = exchangeRate;
            this.convertedAmount = convertedAmount;
        }

        public BigDecimal getConvertedAmount() {
            return convertedAmount;
        }

        public BigDecimal getExchangeRate() {
            return exchangeRate;
        }
    }


}
