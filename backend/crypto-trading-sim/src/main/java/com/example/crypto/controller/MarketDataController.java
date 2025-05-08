package com.example.crypto.controller;

import com.example.crypto.websocketclient.KrakenWebClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/marketData")
public class MarketDataController {
    private final KrakenWebClient krakenWebClient;

    public MarketDataController(KrakenWebClient krakenWebClient) {
        this.krakenWebClient = krakenWebClient;
    }
    @GetMapping
    public Map<String, Double> getMarketData() {
        return krakenWebClient.getMarketData();
    }
}
