package com.example.holding_service.holding_service.controller;


import com.example.holding_service.holding_service.model.CryptoHolding;
import com.example.holding_service.holding_service.model.TransactionType;
import com.example.holding_service.holding_service.service.CryptoHoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/holdings")
public class CryptoHoldingController {

    @Autowired
    private CryptoHoldingService holdingService;

    @GetMapping
    public List<CryptoHolding> getUserHoldings(@RequestParam long userId) {
        return holdingService.getHoldingsOfUser(userId);
    }
    /**
     * Endpoint to delete all holdings of a specific user
     * @param userId ID of the user
     * @return confirmation message
     */
    @DeleteMapping("/{userId}")
    public String deleteUserHoldings(@PathVariable long userId) {
        holdingService.deleteAllHoldingsOfUser(userId);
        System.out.println("Delete holdings");
        return "All holdings for user " + userId + " deleted.";
    }
    /**
     * Get the quantity of a specific crypto ticker for a user
     */
    @GetMapping("/quantity")
    public BigDecimal getTickerQuantity(@RequestParam long userId, @RequestParam String ticker) {
        return holdingService.getTickerQuantity(userId, ticker);
    }

    /**
     * Get the average price of a specific crypto ticker for a user
     */
    @GetMapping("/average-price")
    public BigDecimal getAveragePrice(@RequestParam long userId, @RequestParam String ticker) {
        return holdingService.getAveragePrice(userId, ticker);
    }

    /**
     * Update a user's holdings based on a transaction
     */
    @PostMapping("/update")
    public String updateHolding(
            @RequestParam long userId,
            @RequestParam String ticker,
            @RequestParam BigDecimal quantity,
            @RequestParam TransactionType type,
            @RequestParam BigDecimal price
    ) {
        holdingService.handleHolding(userId, ticker, quantity, type, price);
        return "Holding updated for user " + userId + ", ticker " + ticker;
    }
}
