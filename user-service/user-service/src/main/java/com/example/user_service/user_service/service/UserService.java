package com.example.user_service.user_service.service;


import com.example.user_service.user_service.client.HoldingClient;
import com.example.user_service.user_service.client.TransactionClient;
import com.example.user_service.user_service.client.UserClient;
import com.example.user_service.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class that handles the business logic for
 * operations related to the user e.g. resetting to default state
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final HoldingClient holdingClient;
    private final TransactionClient transactionClient;
    private final UserClient userClient;

    public UserService(UserRepository userRepository,
                       HoldingClient holdingClient,
                       TransactionClient transactionClient,
                       UserClient userClient) {
        this.userRepository = userRepository;
        this.holdingClient = holdingClient;
        this.transactionClient = transactionClient;
        this.userClient = userClient;
    }


    /**
     * Resets the balance to default and deletes associated
     * transactions and holdings
     * @param userId the ID of the user that is going to be reset
     */
    public void resetAccount(long userId){
        userRepository.resetBalance(userId);
        //userClient.resetUser(userId);
        transactionClient.deleteAllUserTransactions(userId);
        holdingClient.deleteAllUserHoldings(userId);
        System.out.println("Received reset");
        //this should become calls to the corresponding services
        //transactionRepository.deleteAllTxs(userId);
        //cryptoHoldingService.deleteAllHoldingsOfUser(userId);
    }

    /**
     * Returns the balance for the user
     * @param userId the ID of the user whose balance is being requested
     * @return the current balance as a {@link BigDecimal}
     */
    public BigDecimal getBalance(long userId){
            return userRepository.getBalanceOfUser(userId);
    }
    public void updateBalance(long userId, BigDecimal newBalance){
        userRepository.updateBalance(userId,newBalance);
    }
}
