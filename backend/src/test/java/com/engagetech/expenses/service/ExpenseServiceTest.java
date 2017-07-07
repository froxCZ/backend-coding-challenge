package com.engagetech.expenses.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.engagetech.expenses.model.Expense;
import com.engagetech.expenses.repository.ExpenseRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private VatService vatService;

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private ExpenseService expenseService;

    LocalDate date = LocalDate.now();
    private BigDecimal gbpAmount;
    private BigDecimal eurGbpRate;
    private Expense expense;

    @Before
    public void setup() {
        gbpAmount = BigDecimal.valueOf(9.32);
        eurGbpRate = BigDecimal.valueOf(1.13);
        expense = new Expense();
        expense.setDate(date);
    }

    @Test
    public void testWithEur() throws Exception {
        Money eurAmount = Money.of(CurrencyUnit.EUR, 10.54);

        when(exchangeService.convertToGBP(eurAmount, date)).thenReturn(
                new ExchangeService.CurrencyConversion(eurGbpRate, gbpAmount));

        expenseService.save(expense, eurAmount);

        verify(expenseRepository, times(1)).save(expense);
        assertThat(expense.getAmount(), is(gbpAmount));
        assertThat(expense.getExchangeRate(), is(eurGbpRate));
    }

    @Test
    public void testWithGbp() throws Exception {
        expenseService.save(expense, Money.of(CurrencyUnit.GBP,gbpAmount));

        verify(expenseRepository, times(1)).save(expense);
        verifyZeroInteractions(exchangeService);
        assertThat(expense.getAmount(), is(gbpAmount));
        assertThat(expense.getExchangeRate(), is(BigDecimal.ONE));
    }

}