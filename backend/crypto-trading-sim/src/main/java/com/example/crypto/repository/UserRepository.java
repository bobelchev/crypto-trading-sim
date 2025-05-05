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
    public BigDecimal getBalanceOfUser(long id){
        //on purpose so that test fails for now
        return new BigDecimal("0");
    }
}
