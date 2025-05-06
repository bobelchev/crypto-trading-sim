package com.example.crypto.service;

import com.example.crypto.repository.CryptoHoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CryptoHoldingService {
    @Autowired
    CryptoHoldingRepository cryptoHoldingRepository;

    /**
     * Handles the business logic of creating or updating existing holding.
     * When holding exist it either increases or decreases the quantity.
     * When holding does not exist it inserts a new holding into the DB.
     * @param userId the ID of the user which holdings will be updated
     * @param cryptoTicker the cryptocurrency ticker (e.g. BTC, ETH)
     * @param quantity the amount by which the holding will be updated
     */
    public void handleHolding(long userId, String cryptoTicker, BigDecimal quantity){

    }
}
