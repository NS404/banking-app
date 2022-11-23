package com.ns.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    private LocalDateTime time;
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    public AccountRequest(User user, Currency currency, LocalDateTime time) {
        this.user = user;
        this.time = time;
        this.currency = currency;
        this.status = RequestStatus.PENDING;
    }
}
