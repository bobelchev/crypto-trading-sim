package com.example.transaction_service.transaction_service.controller;


import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.model.Transaction;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<Transaction> getTransactions(@RequestParam long userId){
        return transactionService.getAllTransactions(userId);
    }

    /**
     * Endpoint for submitting a transaction
     * @param transactionRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestBody TransactionDTO transactionRequest){
        transactionService.makeTx(transactionRequest);
        return ResponseEntity.ok("Transaction successful.");
    }
    /**
     * Endpoint to delete all transactions of a specific user
     * @param userId ID of the user
     * @return HTTP 200 with confirmation message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserTransactions(@PathVariable long userId) {
        transactionRepository.deleteAllTxs(userId);
        System.out.println("Delete transactions");
        return ResponseEntity.ok("All transactions for user " + userId + " deleted.");
    }


}
