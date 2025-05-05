package com.example.crypto.model;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * POJO to represent transaction made by the user to buy/sell crypto
 * In the rest of the code might be referred to as tx for short
 */
@Data
public class Transaction {
    private long id;
    //the user that the transaction belongs to
    private long userId;
    private String cryptoTicker;
    private BigDecimal quantity;
    //this not price per coin but total
    private BigDecimal price;
    private LocalDateTime timestamp;
    private TransactionType transactionType;
    public Transaction(long userId, String cryptoTicker, BigDecimal quantity,
                       BigDecimal price, LocalDateTime timestamp, TransactionType type) {
        this.userId = userId;
        this.cryptoTicker = cryptoTicker;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.transactionType = type;
    }
    public Transaction(long id, long userId, String cryptoTicker, BigDecimal quantity,
                       BigDecimal price, LocalDateTime timestamp, TransactionType type) {
        this(userId,cryptoTicker,quantity,price,timestamp,type);
        this.id = id;
    }



}
