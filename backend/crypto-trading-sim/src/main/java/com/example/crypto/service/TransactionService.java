package com.example.crypto.service;

import com.example.crypto.repository.CryptoHoldingRepository;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class that handles the logic of transactions made by the user.
 */
@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CryptoHoldingService cryptoHoldingService;

    /**
     * Handles the business logic of a buy transaction.
     * Deducts from the account balance the total amount
     * calculated as {@code quantity x price}.
     * Adds a new holding to the crypto holding or updates
     * existing one by increasing its quantity.
     * Creates a transaction of sell type and saves it in the DB.
     * @param userId the ID of the user making the transaction
     * @param cryptoTicker the ticker bought e.g. BTC, ETH, etc.
     * @param quantity the quantity bought
     * @param price the price of 1 coin from the ticker
     */
    public void buy(long userId, String cryptoTicker, BigDecimal quantity, BigDecimal price){
        //insert transaction
        //handle the holding logic if it exist insert new one otherwise update
        //delegate that to CryptoService
        //update the balance

    }
    /**
     * Handles the business logic of a sell transaction.
     * Adds to the account balance the total amount
     * calculated as {@code quantity x price}.
     * Removes a holding from the holdings or updates
     * existing one by decreasing its quantity.
     * Creates a transaction sell type and saves it in the DB.
     * @param userId the ID of the user making the transaction
     * @param cryptoTicker the ticker bought e.g. BTC, ETH, etc.
     * @param quantity the quantity bought
     * @param price the price of 1 coin from the ticker
     */
    public void sell(long userId, String cryptoTicker, BigDecimal quantity, BigDecimal price){

    }
}
