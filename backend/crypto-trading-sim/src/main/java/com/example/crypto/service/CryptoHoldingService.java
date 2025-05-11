package com.example.crypto.service;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.CryptoHoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public void handleHolding(long userId, String cryptoTicker, BigDecimal quantity, TransactionType type){
        CryptoHolding cryptoHolding = cryptoHoldingRepository.getSingleHoldingByTickerAndUserId(userId,cryptoTicker);
        //if it does not exist we will update existing row
        if (cryptoHolding!=null){
            BigDecimal oldQuantity = cryptoHolding.getQuantity();
            //depending on if the tx was BUY or sell we will add or substract
            BigDecimal newQuantity = (type==TransactionType.BUY)?oldQuantity.add(quantity):oldQuantity.subtract(quantity);
            cryptoHoldingRepository.updateHolding(new CryptoHolding(userId,cryptoTicker,newQuantity));
        } else {
            cryptoHoldingRepository.insertHolding(new CryptoHolding(userId,cryptoTicker,quantity));
        }

    }
    public void deleteAllHoldingsOfUser(long userId){
        cryptoHoldingRepository.deleteHoldings(userId);
    }
    public BigDecimal getTickerQuantity(long userId, String cryptoTicker) {
        CryptoHolding holding = cryptoHoldingRepository.getSingleHoldingByTickerAndUserId(userId, cryptoTicker);
        return (holding != null) ? holding.getQuantity() : BigDecimal.ZERO;
    }

    public List<CryptoHolding> getHoldingsOfUser(long userId){
        return cryptoHoldingRepository.getAllUserHoldings(userId);
    }
}
