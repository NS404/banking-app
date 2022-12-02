package com.ns.bankingapp.model.cardRequest;

import com.ns.bankingapp.model.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class CardRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    protected LocalDateTime time;

    @Enumerated(EnumType.STRING)
    protected RequestStatus status;

    public CardRequest(LocalDateTime time){
        this.time = time;
        this.status = RequestStatus.PENDING;
    }
}
