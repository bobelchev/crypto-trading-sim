package com.example.crypto.controller;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.service.CryptoHoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/holdings")
public class CryptoHoldingController {

    @Autowired
    private CryptoHoldingService holdingService;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public List<CryptoHolding> getUserHoldings(@RequestParam long userId) {
        return holdingService.getHoldingsOfUser(userId);
    }
}
