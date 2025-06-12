package com.example.user_service.user_service.controller;

import com.example.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller that handles user-related operations,
 * such as retrieving the user's balance and resetting their account.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Endpoint to return the current balance.
     * @param userId
     * @return
     */
    @GetMapping("/balance")
    public BigDecimal getUserBalance(@RequestParam long userId) {
        return userService.getBalance(userId);
    }

    /**
     * Endpoint to reset the account of a user
     * including deleting all txs and holdings.
     * @param userId
     */
    @PostMapping("/reset")
    public void resetUserAccount(@RequestParam long userId) {
        userService.resetAccount(userId);
    }
    /**
     * Endpoint for updating the balance of a user account to a specified value.
     * @param userId      the ID of the user whose balance is to be updated
     * @param newBalance  the new balance value to set for the user
     */
    @PostMapping("/updateBalance")
    public void updateBalance(@RequestParam long userId, @RequestParam BigDecimal newBalance) {
        userService.updateBalance(userId,newBalance);
    }
}
