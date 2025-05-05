package com.example.crypto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Returns the balance of a User according to its id
     * TODO: implement SQL query
     */
    public BigDecimal getBalanceOfUser(long userId){
        //on purpose so that test fails for now
        return new BigDecimal("0");
    }

    /**
     * Updates the balance of the given user in the DB
     * TODO:implement SQL query
     * @param userId
     * @param newBalance
     */
    public void updateBalance(long userId, BigDecimal newBalance){

    }
}
