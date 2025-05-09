package com.example.crypto.controller;

import com.example.crypto.controller.dto.TransactionDTO;
import com.example.crypto.model.Transaction;
import com.example.crypto.service.TransactionService;
import com.example.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller to handle transactions made by the user
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * Endpoint for submitting a transaction
     * @param transactionRequest
     * @return
     */
    @PostMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> makeTransaction(@RequestBody TransactionDTO transactionRequest){
        transactionService.makeTx(
                transactionRequest.getUserId(),
                transactionRequest.getCryptoTicker(),
                transactionRequest.getQuantity(),
                transactionRequest.getPrice(),
                transactionRequest.getType()
        );
        return ResponseEntity.ok("Transaction successful.");
    }


}
