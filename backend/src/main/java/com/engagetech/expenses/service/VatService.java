package com.engagetech.expenses.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VatService {
    @Value("${app.vat}")
    private final BigDecimal VAT_RATE = null;


    public BigDecimal calculateVatAmount(BigDecimal amount) {
        return amount.multiply(VAT_RATE);
    }

}
