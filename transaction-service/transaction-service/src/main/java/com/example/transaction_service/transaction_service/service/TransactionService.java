package com.example.transaction_service.transaction_service.service;


import com.example.transaction_service.transaction_service.client.HoldingClient;
import com.example.transaction_service.transaction_service.client.UserClient;
import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.model.Transaction;
import com.example.transaction_service.transaction_service.model.TransactionType;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.validation.TransactionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class that handles the logic of transactions made by the user.
 */
@Service
public class TransactionService {

    private final TransactionValidator transactionValidator;
    private final UserClient userClient;
    private final HoldingClient holdingClient;
    private final TransactionRepository transactionRepository;



    public TransactionService(TransactionRepository transactionRepository,
                              TransactionValidator transactionValidator,
                              UserClient userClient,
                              HoldingClient holdingClient) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
        this.userClient = userClient;
        this.holdingClient = holdingClient;
    }

    public List<Transaction> getAllTransactions(long userId){
        return transactionRepository.getAllTxForUser(userId);
    }

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
    @Transactional
    public void makeTx(TransactionDTO transaction){
        BigDecimal cost = transaction.getPrice().multiply(transaction.getQuantity());
        //will leave both until user service is stable
        //BigDecimal availableBalance = userRepository.getBalanceOfUser(transaction.getUserId());
        BigDecimal availableBalance = userClient.getUserBalance(transaction.getUserId());
        System.out.println("Test Balance: " + availableBalance);
        //BigDecimal currentTickerQuantity = cryptoHoldingService.getTickerQuantity(transaction.getUserId(),transaction.getCryptoTicker());
        BigDecimal currentTickerQuantity = holdingClient.getTickerQuantity(
                transaction.getUserId(), transaction.getCryptoTicker()
        );

        System.out.println("Current ticker quantity from service: " + currentTickerQuantity);

        transactionValidator.validate(transaction,availableBalance,currentTickerQuantity);
        BigDecimal newBalance = calculateNewBalance(transaction.getType(),availableBalance,cost);

        //userRepository.updateBalance(transaction.getUserId(),newBalance);

        userClient.updateBalance(transaction.getUserId(), newBalance);
        System.out.println("Send new balance " + newBalance);
        //read the price before the holding gets deleted
        //BigDecimal averagePrice = cryptoHoldingService.getAveragePrice(transaction.getUserId(), transaction.getCryptoTicker());
        BigDecimal averagePrice = holdingClient.getAveragePrice(
                transaction.getUserId(), transaction.getCryptoTicker()
        );

        System.out.println("Average price from service: " + averagePrice);
        //cryptoHoldingService.handleHolding(transaction.getUserId(),transaction.getCryptoTicker(),transaction.getQuantity(),transaction.getType(),transaction.getPrice());
        System.out.println("Updating the holding service...");
        holdingClient.updateHolding(
                transaction.getUserId(),
                transaction.getCryptoTicker(),
                transaction.getQuantity(),
                transaction.getType(),
                transaction.getPrice()
        );
        BigDecimal profitOrLoss = calculatePnL(transaction.getType(),transaction.getPrice(),averagePrice,transaction.getQuantity());
        insertTx(transaction, profitOrLoss);

    }
   private BigDecimal calculateNewBalance(TransactionType type, BigDecimal availableBalance, BigDecimal cost){
        return (type.equals(TransactionType.BUY))?availableBalance.subtract(cost):availableBalance.add(cost);
   }
   private BigDecimal calculatePnL(TransactionType type,BigDecimal price, BigDecimal averagePrice, BigDecimal quantity){
        return type == TransactionType.SELL?price.subtract(averagePrice).multiply(quantity):BigDecimal.ZERO;
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
