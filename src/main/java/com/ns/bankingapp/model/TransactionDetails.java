package com.ns.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    private String fromIBAN;
    private String toIBAN;
    private BigDecimal amount;
    private Currency currency;
}
