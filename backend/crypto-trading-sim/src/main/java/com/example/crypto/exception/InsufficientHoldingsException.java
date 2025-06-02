package com.example.crypto.exception;

public class InsufficientHoldingsException extends RuntimeException{
    public InsufficientHoldingsException(String message) {
        super(message);
    }
}
