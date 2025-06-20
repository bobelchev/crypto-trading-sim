package com.example.transaction_service.transaction_service.service.validation;


import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.exception.InsufficientBalanceException;
import com.example.transaction_service.transaction_service.exception.InsufficientHoldingsException;
import com.example.transaction_service.transaction_service.exception.InvalidTransactionException;
import com.example.transaction_service.transaction_service.model.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {
    public void validate(TransactionDTO transaction, BigDecimal balance, BigDecimal holdings) {
        if (transaction.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Quantity must be a positive number.");
        }
        BigDecimal cost = transaction.getPrice().multiply(transaction.getQuantity());
        if (transaction.getType() == TransactionType.BUY && cost.compareTo(balance) > 0) {
            throw new InsufficientBalanceException("Insufficient balance to complete the purchase.");
        }
        if (transaction.getType() == TransactionType.SELL && transaction.getQuantity().compareTo(holdings) > 0) {
            throw new InsufficientHoldingsException("Insufficient holdings to complete the sale.");
        }
    }
}

