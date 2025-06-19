package com.example.transaction_service.transaction_service.controller;


import com.example.transaction_service.transaction_service.exception.InsufficientBalanceException;
import com.example.transaction_service.transaction_service.exception.InsufficientHoldingsException;
import com.example.transaction_service.transaction_service.exception.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IllegalTransaction {
    @ExceptionHandler({
            InsufficientBalanceException.class,
            InsufficientHoldingsException.class,
            InvalidTransactionException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalTransactionHandler(IllegalStateException ex) {
        return ex.getMessage();
    }

}
