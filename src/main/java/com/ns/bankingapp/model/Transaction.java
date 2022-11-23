package com.ns.bankingapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Account account;
    private BigDecimal amount;
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(Account account, BigDecimal amount, Currency currency, TransactionType transactionType) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
    }
}
