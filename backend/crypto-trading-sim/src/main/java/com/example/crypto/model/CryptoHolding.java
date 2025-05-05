package com.example.crypto.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * POJO that represents a single crypto holding of a user (bought crypto)
 */
@Data
public class CryptoHolding {
    private long id;
    private long userId;
    private String cryptoTicker;
    private BigDecimal quantity;

}
