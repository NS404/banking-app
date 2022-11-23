package com.ns.bankingapp.model.cardRequest;

import com.ns.bankingapp.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCardRequest extends CardRequest {

    @ManyToOne
    private User user;

    private Currency currency;


    public CreditCardRequest(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
    }

}
