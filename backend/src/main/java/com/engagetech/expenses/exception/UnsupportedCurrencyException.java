package com.engagetech.expenses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String currencyCode) {
        super("Unsupported currency: " + currencyCode);
    }
}
