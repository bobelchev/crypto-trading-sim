package com.example.user_service.user_service.service;


import com.example.user_service.user_service.client.HoldingClient;
import com.example.user_service.user_service.client.TransactionClient;
import com.example.user_service.user_service.client.UserClient;
import com.example.user_service.user_service.model.User;
import com.example.user_service.user_service.repository.UserRepository;
import com.example.user_service.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,
                       HoldingClient holdingClient,
                       TransactionClient transactionClient,
                       UserClient userClient,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.holdingClient = holdingClient;
        this.transactionClient = transactionClient;
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
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
    public String login(String username, String password) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = optUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return JwtUtil.generateToken(user.getId());
    }
    public void register(String username, String email, String password){
        Optional<User> optUser = userRepository.findByUsername(username);
        if (!optUser.isEmpty()) {
            throw new IllegalArgumentException("Username exists! Choose another one");
        }
        String hashedPassword = passwordEncoder.encode(password);
        userRepository.addUser(username,email,hashedPassword);


    }

}
