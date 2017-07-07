package com.engagetech.expenses.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engagetech.expenses.model.Expense;
import com.engagetech.expenses.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private VatService vatService;

    @Autowired
    private ExchangeService exchangeService;

    /**
     * Saves Expense with specified amount. If the amount is not in GBP, it is converted to GBP.
     */
    @Transactional
    public void save(Expense expense, Money originalAmount) {
        BigDecimal amountInGBP;
        BigDecimal exchangeRate;
        if (originalAmount.getCurrencyUnit() == CurrencyUnit.GBP) {
            amountInGBP = originalAmount.getAmount();
            exchangeRate = BigDecimal.ONE;
        } else {
            ExchangeService.CurrencyConversion currencyConversion = exchangeService
                    .convertToGBP(originalAmount, expense.getDate());
            exchangeRate = currencyConversion.getExchangeRate();
            amountInGBP = currencyConversion.getConvertedAmount();
        }
        expense.setAmount(amountInGBP);
        expense.setExchangeRate(exchangeRate);

        BigDecimal vat = vatService.calculateVatAmount(expense.getAmount());
        expense.setVat(vat);

        expenseRepository.save(expense);
    }

    @Transactional
    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }
}
