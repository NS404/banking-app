package com.ns.bankingapp.model.cardRequest;

import com.ns.bankingapp.model.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class CardRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected LocalDateTime time;
    protected RequestStatus status;

    public CardRequest(LocalDateTime time){
        this.time = time;
        this.status = RequestStatus.PENDING;
    }
}
