package com.example.crypto.repository;

import com.example.crypto.model.CryptoHolding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CryptoHoldingRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Returns complete portfolio of user from DB
     * TODO: implement SQL query
     * @param userId
     * @return
     */
    public List<CryptoHolding> getAllUserHoldings(long userId){
        return Collections.emptyList();
    }

    /**
     * Returns a single holding according to id from DB
     * TODO: implement SQL query
     * @param holdingId
     * @return
     */
    public CryptoHolding getSingleHolding(long holdingId){
        return null;
    }

    /**
     * Inserts a new holding into the DB
     * TODO: implement SQL query
     * @param cryptoHolding
     * @return
     */
    public long insertHolding(CryptoHolding cryptoHolding){
        return 1L;
    }

    /**
     * Updates existing holding in the DB
     * TODO: implement SQL query
     * @param cryptoHolding
     * @return
     */
    public long updateHolding(CryptoHolding cryptoHolding){
        return 1L;
    }
}
