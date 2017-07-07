package com.engagetech.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.engagetech.expenses.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
