package com.engagetech.expenses.rest.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engagetech.expenses.exception.UnsupportedCurrencyException;
import com.engagetech.expenses.service.ExchangeService;

@Component
public class AmountCurrencyParser {
    /**
     * Matches amount with optional currency. Examples: 12.1, 12, 12 EUR, 12.6 EUR
     */
    private final String regex = "(\\d+[\\.]?\\d*)(\\s*[a-zA-Z]{3})?";
    private final Pattern pattern = Pattern.compile(regex);

    @Autowired
    private ExchangeService exchangeService;

    /**
     * Returns money object with amount and currency unit. Throws UnsupportedCurrencyException in case the currency
     * is not supported.
     */
    public Money parseAmountWithCurrency(String amountWithCurrency) {
        amountWithCurrency = amountWithCurrency.trim().replaceAll(",", ".");//Treat comma as a dot
        Matcher matcher = pattern.matcher(amountWithCurrency);
        if (!matcher.matches()) {
            throw new ValidationException("Invalid amount format.");
        }
        BigDecimal amount = new BigDecimal(matcher.group(1));
        CurrencyUnit currency;
        if (matcher.group(2) == null) {
            //specified without currency. GBP default.
            currency = CurrencyUnit.GBP;
        } else {
            //specified with currency
            String currencyCode = matcher.group(2).trim().toUpperCase();
            currency = getCurrencyUnit(currencyCode);
        }

        return Money.of(currency, amount, RoundingMode.HALF_EVEN);
    }

    private CurrencyUnit getCurrencyUnit(String currencyCode) {
        if (!exchangeService.isCurrencySupported(currencyCode)) {
            throw new UnsupportedCurrencyException(currencyCode);
        }

        return CurrencyUnit.getInstance(currencyCode);

    }
}
