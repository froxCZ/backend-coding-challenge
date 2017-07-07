package com.engagetech.expenses.rest;

import java.util.List;

import javax.validation.Valid;

import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.engagetech.expenses.mapper.OrikaMapper;
import com.engagetech.expenses.model.Expense;
import com.engagetech.expenses.rest.dto.ExpenseDto;
import com.engagetech.expenses.rest.dto.SaveExpenseDto;
import com.engagetech.expenses.rest.helper.AmountCurrencyParser;
import com.engagetech.expenses.service.ExpenseService;

import static com.engagetech.expenses.rest.RestConfig.APP_PATH;

@RestController
@RequestMapping(APP_PATH + "/expenses")
public class ExpenseRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OrikaMapper mapper;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private AmountCurrencyParser amountCurrencyParser;

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveExpense(@Valid @RequestBody SaveExpenseDto saveExpenseDto) {
        Expense expense = new Expense();
        expense.setReason(saveExpenseDto.getReason());
        expense.setDate(saveExpenseDto.getDate());
        Money amount = amountCurrencyParser.parseAmountWithCurrency(saveExpenseDto.getAmountWithCurrency());
        expense.setOriginalCurrency(amount.getCurrencyUnit().getCurrencyCode());

        expenseService.save(expense, amount);
        logger.info("Saved expense.");

    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ExpenseDto> getExpenses() {
        return mapper.mapAsList(expenseService.getExpenses(), ExpenseDto.class);
    }

}