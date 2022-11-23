package com.ns.bankingapp.model.cardRequest;

import com.ns.bankingapp.model.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DebitCardRequest extends CardRequest {

    @ManyToOne
    private Account account;


    public DebitCardRequest(Account account, LocalDateTime time) {
        super(time);
        this.account = account;
    }

}
