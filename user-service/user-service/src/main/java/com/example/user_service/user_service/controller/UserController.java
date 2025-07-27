package com.example.user_service.user_service.controller;

import com.example.user_service.user_service.service.UserService;
import com.example.user_service.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * Endpoint to return the current user's balance.
     * <p>
     * Expects a valid JWT token in the Authorization header.
     * The user ID is extracted from the token's subject claim.
     * </p>
     *
     * @param authHeader the Authorization header containing a Bearer token
     * @return the current balance of the authenticated user
     * @throws ResponseStatusException if the token is missing, invalid, or the user ID is malformed
     */
    @GetMapping("/balance")
    public BigDecimal getUserBalance(@RequestHeader("Authorization") String authHeader) {
        try {
        String token = authHeader.substring(7);
        String userIdStr = JwtUtil.getIdFromToken(token);
        Long userId = Long.parseLong(userIdStr);

        return userService.getBalance(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", e);
        }
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
        System.out.println("Received the request");
        userService.updateBalance(userId,newBalance);
    }
}
