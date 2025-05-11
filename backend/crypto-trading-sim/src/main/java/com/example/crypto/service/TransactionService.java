package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

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

    public List<Transaction> getAllTransactions(long userId){
        return transactionRepository.getAllTxForUser(userId);
    }

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
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Quantity must be a positive number.");
        }
        BigDecimal cost = price.multiply(quantity);
        BigDecimal availableBalance = userRepository.getBalanceOfUser(userId);
        BigDecimal currentTickerQuantity = cryptoHoldingService.getTickerQuantity(userId,cryptoTicker);
        // TODO define custom exception
        /*
        specify buy or sell because in case of selling for cost
        bigger than the balance it will still throw which is incorrect
         */
        if(type == TransactionType.BUY  && cost.compareTo(availableBalance)>0){
            throw new IllegalStateException("Insufficient balance to complete the purchase.");
        } else if (type == TransactionType.SELL && quantity.compareTo(currentTickerQuantity) > 0) {
            throw new IllegalStateException("Insufficient holdings to complete the sale.");
        }
        BigDecimal newBalance = (type.equals(TransactionType.BUY))?availableBalance.subtract(cost):availableBalance.add(cost);
        userRepository.updateBalance(userId,newBalance);
        //before holding got deleted before we can read the average price
        BigDecimal averagePrice = cryptoHoldingService.getAveragePrice(userId, cryptoTicker);
        cryptoHoldingService.handleHolding(userId,cryptoTicker,quantity,type,price);
        if (type == TransactionType.SELL){
            BigDecimal profitOrLoss = price.subtract(averagePrice).multiply(quantity);
            insertTx(userId, cryptoTicker, quantity, price, type, profitOrLoss);
        } else {
            insertTx(userId,cryptoTicker,quantity,price,type,BigDecimal.ZERO);
        }

    }

   private void insertTx(long userId, String cryptoTicker, BigDecimal quantity, BigDecimal price, TransactionType type, BigDecimal pNl){
        transactionRepository.insertTx(
                new Transaction(
                        userId,cryptoTicker,
                        quantity,price,
                        LocalDateTime.now(),
                        type,
                        pNl
                )
        );
   }
}
