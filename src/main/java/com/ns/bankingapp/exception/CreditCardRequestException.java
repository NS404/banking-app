package com.ns.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CreditCardRequestException extends Exception {
    public CreditCardRequestException(String message) {
        super(message);
    }
}
