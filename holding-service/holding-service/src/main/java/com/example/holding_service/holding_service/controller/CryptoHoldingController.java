package com.example.holding_service.holding_service.controller;


import com.example.holding_service.holding_service.model.CryptoHolding;
import com.example.holding_service.holding_service.service.CryptoHoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
