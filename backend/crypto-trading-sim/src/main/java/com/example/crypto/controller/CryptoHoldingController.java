package com.example.crypto.controller;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.service.CryptoHoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/holdings")
public class CryptoHoldingController {

    @Autowired
    private CryptoHoldingService holdingService;

    @GetMapping
    public List<CryptoHolding> getUserHoldings(@RequestParam long userId) {
        return Collections.emptyList();
    }
}
