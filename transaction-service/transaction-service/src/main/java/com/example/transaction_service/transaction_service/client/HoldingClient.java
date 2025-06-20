package com.example.transaction_service.transaction_service.client;

import com.example.transaction_service.transaction_service.model.TransactionType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("holding-service")
public interface HoldingClient {
    @GetMapping("/holdings")
    List<Object> getUserHoldings(@RequestParam("userId") long userId);


    @GetMapping("/holdings/quantity")
    BigDecimal getTickerQuantity(
            @RequestParam("userId") long userId,
            @RequestParam("ticker") String ticker
    );

    @GetMapping("/holdings/average-price")
    BigDecimal getAveragePrice(
            @RequestParam("userId") long userId,
            @RequestParam("ticker") String ticker
    );

    @PostMapping("/holdings/update")
    void updateHolding(
            @RequestParam("userId") long userId,
            @RequestParam("ticker") String ticker,
            @RequestParam("quantity") BigDecimal quantity,
            @RequestParam("type") TransactionType type,
            @RequestParam("price") BigDecimal price
    );
}
