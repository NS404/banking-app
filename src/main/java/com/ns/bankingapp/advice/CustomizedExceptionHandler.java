package com.ns.bankingapp.advice;

import com.ns.bankingapp.exception.AccountRequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ControllerAdvice
public class CustomizedExceptionHandler {


    @ExceptionHandler(SQLException.class)
    public void handle(){

    }



}
