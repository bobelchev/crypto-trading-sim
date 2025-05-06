package com.example.crypto.service;

import com.example.crypto.repository.CryptoHoldingRepository;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class that handles the business logic for
 * operations related to the user e.g. resetting to default state
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CryptoHoldingRepository cryptoHoldingRepository;

    /**
     * Resets the balance to default and deletes associated
     * transactions and holdings
     * @param userId the ID of the user that is going to be reset
     */
    public void resetAccount(long userId){

    }

    /**
     * Returns the balance for the user
     * @param userId the ID of the user whose balance is being requested
     * @return the current balance as a {@link BigDecimal}
     */
    public BigDecimal getBalance(long userId){
            return new BigDecimal("0.000000");
    }
}
