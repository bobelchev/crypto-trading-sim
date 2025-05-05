package com.example.crypto.repository;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.repository.mapper.CryptoHoldingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CryptoHoldingRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Returns complete portfolio of user from DB
     * @param userId
     * @return list of (all)crypto holdings
     */
    public List<CryptoHolding> getAllUserHoldings(long userId){
        String sql = "SELECT * FROM holdings WHERE user_id=?";
        return jdbcTemplate.query(sql,new Object[]{userId},new CryptoHoldingMapper());
    }

    /**
     * Returns a single holding according to id from DB
     * @param holdingId
     * @return A single holding according to id
     */
    public CryptoHolding getSingleHolding(long holdingId){
        String sql = "SELECT * FROM holdings WHERE id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{holdingId},new CryptoHoldingMapper());
    }

    /**
     * Inserts a new holding into the DB
     * @param cryptoHolding
     */
    public void insertHolding(CryptoHolding cryptoHolding){
        String sql = """
        INSERT INTO holdings (user_id, crypto_ticker, quantity)
        VALUES (?, ?, ?)
    """;
        jdbcTemplate.update(sql,
                cryptoHolding.getUserId(),cryptoHolding.getCryptoTicker(),
                cryptoHolding.getQuantity());

    }

    /**
     * Updates existing holding in the DB
     * @param cryptoHolding
     */
    public void updateHolding(CryptoHolding cryptoHolding){
        String sql = "UPDATE holdings SET quantity = ? WHERE user_id = ? AND crypto_ticker = ?";
        jdbcTemplate.update(sql,
                cryptoHolding.getQuantity(),cryptoHolding.getUserId(),
                cryptoHolding.getCryptoTicker());

    }

    /**
     * Deletes all holdings of a user
     * @param userId
     */
    public void deleteHoldings(long userId){
        String sql = "DELETE FROM holdings WHERE user_id = ?";
        jdbcTemplate.update(sql,userId);
    }
}
