package com.example.crypto.controller.dto;

import com.example.crypto.model.TransactionType;
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

