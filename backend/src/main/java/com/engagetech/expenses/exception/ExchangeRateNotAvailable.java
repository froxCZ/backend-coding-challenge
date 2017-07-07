package com.engagetech.expenses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.SERVICE_UNAVAILABLE)
public class ExchangeRateNotAvailable extends RuntimeException {
    public ExchangeRateNotAvailable() {
        super("Exchange rate for specified currency and date is not available. Please try again later.");
    }
}
