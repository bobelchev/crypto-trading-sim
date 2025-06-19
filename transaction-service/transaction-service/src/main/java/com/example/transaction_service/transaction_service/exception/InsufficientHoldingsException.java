package com.example.transaction_service.transaction_service.exception;

public class InsufficientHoldingsException extends RuntimeException{
    public InsufficientHoldingsException(String message) {
        super(message);
    }
}
