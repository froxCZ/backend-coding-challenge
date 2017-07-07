package com.engagetech.expenses.integration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.engagetech.expenses.rest.dto.ExpenseDto;
import com.engagetech.expenses.rest.dto.SaveExpenseDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class ExpensesIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    private SaveExpenseDto saveExpense1;
    private SaveExpenseDto saveExpense2;
    private ExpenseDto expense1;
    private ExpenseDto expense2;

    @Before
    public void setUp() throws Exception {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        saveExpense1 = new SaveExpenseDto();
        saveExpense1.setDate(LocalDate.parse("01/05/2015", formatter));
        saveExpense1.setReason("Bus Ticket");
        saveExpense1.setAmountWithCurrency("12.5");
        expense1 = saveExpenseToExpenseDto(saveExpense1);
        expense1.setAmount(new BigDecimal("12.50"));
        expense1.setVat(new BigDecimal("2.50"));

        saveExpense2 = new SaveExpenseDto();
        saveExpense2.setDate(LocalDate.parse("08/09/2016", formatter));
        saveExpense2.setReason("Train Ticket");
        saveExpense2.setAmountWithCurrency("12.5 EUR");
        expense2 = saveExpenseToExpenseDto(saveExpense2);
        expense2.setAmount(new BigDecimal("11.06"));
        expense2.setVat(new BigDecimal("2.21"));
    }

    @Test
    public void test01_createExpenseGbpAndEur() {
        createExpense(saveExpense1);
        verifyExpenses(expense1);

        createExpense(saveExpense2);
        verifyExpenses(expense1, expense2);
    }

    private void createExpense(SaveExpenseDto saveExpenseDto) {
        ResponseEntity<String> response = restTemplate.postForEntity("/app/expenses", saveExpenseDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    private void verifyExpenses(ExpenseDto... expenseDto) {
        restTemplate.getForEntity("/app/expenses", String.class);
        List<ExpenseDto> expenses = restTemplate.exchange("/app/expenses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExpenseDto>>() {
                }).getBody();
        assertThat(expenses, containsInAnyOrder(expenseDto));
    }

    private ExpenseDto saveExpenseToExpenseDto(SaveExpenseDto saveExpenseDto) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setReason(saveExpenseDto.getReason());
        expenseDto.setDate(saveExpenseDto.getDate());
        return expenseDto;
    }

}