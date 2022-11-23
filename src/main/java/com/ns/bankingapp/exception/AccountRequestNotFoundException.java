package com.ns.bankingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountRequestNotFoundException extends Exception {

    public AccountRequestNotFoundException(String message){
        super(message);
    }
}
