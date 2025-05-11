package com.example.crypto.service;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.CryptoHoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public void handleHolding(long userId, String cryptoTicker, BigDecimal quantity, TransactionType type, BigDecimal price){
        CryptoHolding cryptoHolding = cryptoHoldingRepository.getSingleHoldingByTickerAndUserId(userId,cryptoTicker);
        //if it does not exist we will update existing row
        if (cryptoHolding!=null){
            BigDecimal oldQuantity = cryptoHolding.getQuantity();
            //depending on if the tx was BUY or sell we will add or substract
            BigDecimal newQuantity = (type==TransactionType.BUY)?oldQuantity.add(quantity):oldQuantity.subtract(quantity);
            //if we sell all from an asset it should be removed
            if (type == TransactionType.SELL && newQuantity.compareTo(BigDecimal.ZERO) == 0) {
                cryptoHoldingRepository.deleteSingleHolding(userId, cryptoTicker);
                return;
            }
            BigDecimal newAvgPrice = cryptoHolding.getAveragePrice();
            //change average only in the case of a BUY
            if (type == TransactionType.BUY) {
                newAvgPrice = calculateNewAveragePrice(oldQuantity, cryptoHolding.getAveragePrice(), quantity, price);
            }
            cryptoHoldingRepository.updateHolding(new CryptoHolding(userId,cryptoTicker,newQuantity,newAvgPrice));
        } else {
            //in case it does not exist the average price is the market price
            cryptoHoldingRepository.insertHolding(new CryptoHolding(userId,cryptoTicker,quantity,price));
        }

    }
    private BigDecimal calculateNewAveragePrice(BigDecimal oldQuantity, BigDecimal oldAvgPrice, BigDecimal addedQuantity, BigDecimal newPrice) {
        BigDecimal totalCost = oldAvgPrice.multiply(oldQuantity).add(newPrice.multiply(addedQuantity));
        BigDecimal newTotalQuantity = oldQuantity.add(addedQuantity);

        if (newTotalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalCost.divide(newTotalQuantity, 6, RoundingMode.HALF_UP);
    }

    /**
     * Deletes all holdings of user. Used for the reset functionality
     * @param userId the ID of the user whose holdings are to be deleted
     */
    public void deleteAllHoldingsOfUser(long userId){
        cryptoHoldingRepository.deleteHoldings(userId);
    }

    /**
     * Fetches the quantity of a specific ticker in the user holdings
     * @param userId
     * @param cryptoTicker
     * @return the current quantity of that ticker, otherwise 0
     */
    public BigDecimal getTickerQuantity(long userId, String cryptoTicker) {
        CryptoHolding holding = cryptoHoldingRepository.getSingleHoldingByTickerAndUserId(userId, cryptoTicker);
        return (holding != null) ? holding.getQuantity() : BigDecimal.ZERO;
    }

    public BigDecimal getAveragePrice(long userId, String cryptoTicker) {
        CryptoHolding holding = cryptoHoldingRepository.getSingleHoldingByTickerAndUserId(userId, cryptoTicker);
        return (holding != null) ? holding.getAveragePrice() : BigDecimal.ZERO;
    }


    public List<CryptoHolding> getHoldingsOfUser(long userId){
        return cryptoHoldingRepository.getAllUserHoldings(userId);
    }
}
