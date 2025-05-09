package com.example.crypto.controller;

import com.example.crypto.service.UserService;
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
    @CrossOrigin(origins = "http://localhost:5174")
    public BigDecimal getUserBalance(@RequestParam long userId) {
        return userService.getBalance(userId);
    }

    /**
     * Endpoint to reset the account of a user
     * including deleting all txs and holdings.
     * @param userId
     */
    @PostMapping("/reset")
    @CrossOrigin(origins = "http://localhost:5174")
    public void resetUserAccount(@RequestParam long userId) {
        userService.resetAccount(userId);
    }
}
