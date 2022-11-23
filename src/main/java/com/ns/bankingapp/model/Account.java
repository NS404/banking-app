package com.ns.bankingapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String IBAN;
    private Currency currency;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Double interest;
    @ManyToOne
    private User client;

    public Account(User user, Currency currency, Double interest,
                   AccountType accountType, String IBAN) {
        this.client = user;
        this.currency = currency;
        this.interest = interest;
        this.accountType = accountType;
        this.balance = BigDecimal.valueOf(10.0);
        this.IBAN = IBAN;
    }






}
