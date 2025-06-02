package com.example.crypto.service;

import com.example.crypto.controller.dto.TransactionDTO;
import com.example.crypto.exception.InsufficientBalanceException;
import com.example.crypto.exception.InsufficientHoldingsException;
import com.example.crypto.exception.InvalidTransactionException;
import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import com.example.crypto.service.validation.TransactionValidator;
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
    @Autowired
    TransactionValidator transactionValidator;

    /**
     * Handles the business logic for both buy and sell transactions.
     * <p>
     * For a <b>BUY</b> transaction:
     * <ul>
     *   <li>Calculates total cost as {@code quantity × price}</li>
     *   <li>Validates if the user has sufficient balance</li>
     *   <li>Deducts the cost from the user's balance</li>
     *   <li>Updates or creates a crypto holding</li>
     *   <li>Records the transaction in the database</li>
     * </ul>
     *
     * For a <b>SELL</b> transaction:
     * <ul>
     *   <li>Validates if the user has enough holdings of the crypto</li>
     *   <li>Increases the user's balance by {@code quantity × price}</li>
     *   <li>Updates the user's holdings</li>
     *   <li>Calculates profit/loss based on average purchase price</li>
     *   <li>Records the transaction in the database</li>
     * </ul>
     *
     * @param transaction a {@link TransactionDTO} containing user ID, crypto ticker, quantity, price, and transaction type
     * @throws IllegalStateException if the transaction is invalid (e.g., insufficient funds or holdings, or non-positive quantity)
     */
    public void makeTx(TransactionDTO transaction){
        BigDecimal cost = transaction.getPrice().multiply(transaction.getQuantity());
        BigDecimal availableBalance = userRepository.getBalanceOfUser(transaction.getUserId());
        BigDecimal currentTickerQuantity = cryptoHoldingService.getTickerQuantity(transaction.getUserId(),transaction.getCryptoTicker());
        transactionValidator.validate(transaction,availableBalance,currentTickerQuantity);
        BigDecimal newBalance = (transaction.getType().equals(TransactionType.BUY))?availableBalance.subtract(cost):availableBalance.add(cost);
        userRepository.updateBalance(transaction.getUserId(),newBalance);
        //read the price before the holding gets deleted
        BigDecimal averagePrice = cryptoHoldingService.getAveragePrice(transaction.getUserId(), transaction.getCryptoTicker());
        cryptoHoldingService.handleHolding(transaction.getUserId(),transaction.getCryptoTicker(),transaction.getQuantity(),transaction.getType(),transaction.getPrice());
        BigDecimal profitOrLoss = transaction.getType() == TransactionType.SELL
                ?calculatePnL(transaction.getPrice(), averagePrice, transaction.getQuantity())
                :BigDecimal.ZERO;
        insertTx(transaction, profitOrLoss);

    }
    public List<Transaction> getAllTransactions(long userId){
        return transactionRepository.getAllTxForUser(userId);
    }
   private BigDecimal calculatePnL(BigDecimal price, BigDecimal averagePrice, BigDecimal quantity){
        return price.subtract(averagePrice).multiply(quantity);
   }
   private void insertTx(TransactionDTO transaction, BigDecimal pNl){
        transactionRepository.insertTx(
                new Transaction(
                        transaction.getUserId(),transaction.getCryptoTicker(),
                        transaction.getQuantity(),transaction.getPrice(),
                        LocalDateTime.now(),
                        transaction.getType(),
                        pNl
                )
        );
   }
}
