package com.engagetech.expenses.rest.helper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.engagetech.expenses.exception.UnsupportedCurrencyException;
import com.engagetech.expenses.service.ExchangeService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmountCurrencyParserTest {

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private AmountCurrencyParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        when(exchangeService.isCurrencySupported("GBP")).thenReturn(true);
        when(exchangeService.isCurrencySupported("EUR")).thenReturn(true);
        when(exchangeService.isCurrencySupported("CZK")).thenReturn(false);
    }

    @Test
    public void testParsing() {
        for (Object[] testCase : testCases()) {
            if (testCase[2] != null) {
                thrown.expect((Class<? extends Throwable>) testCase[2]);
            }
            assertThat(parser.parseAmountWithCurrency((String) testCase[0]), is(testCase[1]));
        }
    }

    private static List<Object[]> testCases() {
        return Arrays.asList(new Object[][] {
                {"8.569111 EUR ", Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(8.57)), null},
                {"8.56 EUR ", Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(8.56)), null},
                {"12.5", Money.of(CurrencyUnit.GBP, BigDecimal.valueOf(12.5)), null},
                {"12.5 EURx", Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(12.5)), ValidationException.class},
                {"12.5EUR", Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(12.5)), null},
                {"13,0 EUR ", Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(13.0)), null},
                {"12,5 ", Money.of(CurrencyUnit.GBP, BigDecimal.valueOf(12.5)), null},
                {"12,5 CZK ", null, UnsupportedCurrencyException.class},
        });
    }

}