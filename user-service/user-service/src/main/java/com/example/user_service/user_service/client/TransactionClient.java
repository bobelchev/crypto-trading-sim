package com.example.user_service.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="transaction-service",contextId = "transactionClient")
public interface TransactionClient {
    @DeleteMapping("/transactions/{userId}")
    void deleteAllUserTransactions(@PathVariable("userId") long userId);
}
