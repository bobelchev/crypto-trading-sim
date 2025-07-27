package com.example.transaction_service.transaction_service.controller;


import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.model.Transaction;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.TransactionService;
import com.example.transaction_service.transaction_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller to handle transactions made by the user
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    /**
     * Endpoint for getting all the txs of a user
     * @param userId
     * @return
     */
    @GetMapping
    public List<Transaction> getTransactions(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        return transactionService.getAllTransactions(userId);
    }

    /**
     * Endpoint for submitting a transaction
     * @param transactionRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody TransactionDTO transactionRequest) {
        Long userId = extractUserIdFromToken(authHeader);
        transactionRequest.setUserId(userId);
        transactionService.makeTx(transactionRequest, authHeader);
        return ResponseEntity.ok("Transaction successful.");
    }
    /**
     * Endpoint to delete all transactions of a specific user
     * @param userId ID of the user
     * @return HTTP 200 with confirmation message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserTransactions(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        transactionRepository.deleteAllTxs(userId);
        return ResponseEntity.ok("All transactions for user " + userId + " deleted.");
    }
    /**
     * Extracts user ID from the JWT Authorization header.
     */
    private Long extractUserIdFromToken(String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String userIdStr = JwtUtil.getIdFromToken(token);
            return Long.parseLong(userIdStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", e);
        }
    }



}
