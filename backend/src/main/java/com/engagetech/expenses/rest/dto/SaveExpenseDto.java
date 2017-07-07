package com.engagetech.expenses.rest.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.engagetech.expenses.model.Expense.REASON_LENGTH;

public class SaveExpenseDto {
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")//Special date format for save request. Fix frontend to comply with rest of API?
    private LocalDate date;

    @NotNull
    @JsonProperty(value = "amount")
    private String amountWithCurrency;

    @NotNull
    @Size(max = REASON_LENGTH)
    private String reason;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAmountWithCurrency() {
        return amountWithCurrency;
    }

    public void setAmountWithCurrency(String amountWithCurrency) {
        this.amountWithCurrency = amountWithCurrency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
