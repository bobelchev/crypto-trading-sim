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

    public CryptoHolding(long userId,String cryptoTicker,BigDecimal quantity){
        this.userId = userId;
        this.cryptoTicker = cryptoTicker;
        this.quantity = quantity;
    }

    public CryptoHolding(long id, long userId,String cryptoTicker,BigDecimal quantity){
        this(userId,cryptoTicker,quantity);
        this.id = id;

    }

}
