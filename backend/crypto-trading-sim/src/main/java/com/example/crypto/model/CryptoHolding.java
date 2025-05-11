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
    private BigDecimal averagePrice;


    public CryptoHolding(long userId,String cryptoTicker,BigDecimal quantity, BigDecimal averagePrice){
        this.userId = userId;
        this.cryptoTicker = cryptoTicker;
        this.quantity = quantity;
        this.averagePrice =averagePrice;
    }

    public CryptoHolding(long id, long userId,String cryptoTicker,BigDecimal quantity,BigDecimal averagePrice){
        this(userId,cryptoTicker,quantity,averagePrice);
        this.id = id;

    }

}
