package com.ns.bankingapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

    public Card(CardType cardType, Account account) {
        this.cardType = cardType;
        this.account = account;
    }
}
