package com.engagetech.expenses.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceTest {

    @Mock
    private FixerExchangeService fixerExchangeService;

    @InjectMocks
    private ExchangeService exchangeService;

    LocalDate date = LocalDate.now();

    @Test
    public void testConvertToGBPWithCache() throws Exception {
        Map<CurrencyUnit, BigDecimal> exchangeRates = new HashMap<>();
        exchangeRates.put(CurrencyUnit.EUR, BigDecimal.valueOf(1.13));

        when(fixerExchangeService.getExchangeRatesForDate(date, ExchangeService.SUPPORTED_CURRENCIES))
                .thenReturn(exchangeRates);

        ExchangeService.CurrencyConversion conversion =
                exchangeService.convertToGBP(Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(101)), date);
        assertThat(conversion.getExchangeRate(), is(BigDecimal.valueOf(1.13)));
        assertThat(conversion.getConvertedAmount(), is(BigDecimal.valueOf(89.38)));

        conversion =
                exchangeService.convertToGBP(Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(115)), date);

        assertThat(conversion.getExchangeRate(), is(BigDecimal.valueOf(1.13)));
        assertThat(conversion.getConvertedAmount(), is(BigDecimal.valueOf(101.77)));

        verify(fixerExchangeService, times(1)).getExchangeRatesForDate(any(), any());//rates are cached, so called once.

    }

}