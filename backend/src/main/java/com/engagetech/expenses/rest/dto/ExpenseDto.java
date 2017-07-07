package com.engagetech.expenses.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ExpenseDto {
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal vat;
    private String reason;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseDto that = (ExpenseDto) o;
        return Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getVat(), that.getVat()) &&
                Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getAmount(), getVat(), getReason());
    }
}
