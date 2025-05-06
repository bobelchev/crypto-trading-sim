package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public void makeTx(long userId, String cryptoTicker, BigDecimal quantity, BigDecimal price, TransactionType type){
        //insert transaction
        //handle the holding logic if it exist insert new one otherwise update
        //delegate that to CryptoService
        //update the balance
        BigDecimal cost = price.multiply(quantity);
        BigDecimal availableBalance = userRepository.getBalanceOfUser(userId);
        BigDecimal currentTickerQuantity = cryptoHoldingService.getTickerQuantity(userId,cryptoTicker);

        if(cost.compareTo(availableBalance)>0){
            throw new IllegalStateException("Insufficient balance to complete the purchase.");
        } else if (type == TransactionType.SELL && quantity.compareTo(currentTickerQuantity) > 0) {
            throw new IllegalStateException("Insufficient holdings to complete the sale.");
        }
        BigDecimal newBalance = (type.equals(TransactionType.BUY))?availableBalance.subtract(cost):availableBalance.add(cost);
        userRepository.updateBalance(userId,newBalance);
        cryptoHoldingService.handleHolding(userId,cryptoTicker,quantity,type);
        insertTx(userId,cryptoTicker,quantity,price,type);
    }

   private void insertTx(long userId, String cryptoTicker, BigDecimal quantity, BigDecimal price, TransactionType type){
        transactionRepository.insertTx(
                new Transaction(
                        userId,cryptoTicker,
                        quantity,price,
                        LocalDateTime.now(), type
                )
        );
   }
}
