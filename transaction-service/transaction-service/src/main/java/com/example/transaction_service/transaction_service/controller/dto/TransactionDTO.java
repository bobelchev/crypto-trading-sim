package com.example.transaction_service.transaction_service.controller.dto;

import com.example.transaction_service.transaction_service.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private long userId;
    private String cryptoTicker;
    private BigDecimal quantity;
    private BigDecimal price;
    private TransactionType type;

}

