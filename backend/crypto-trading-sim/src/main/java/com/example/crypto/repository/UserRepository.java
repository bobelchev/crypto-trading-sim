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
     * TODO: implement no such user found logic
     */
    public BigDecimal getBalanceOfUser(long userId){
        //on purpose so that test fails for now
        String sql = "SELECT balance FROM users WHERE id=?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql,
                                    BigDecimal.class,
                                    userId);
        return balance;
    }

    /**
     * Updates the balance of the given user in the DB
     * TODO:implement defensive programming for negative balance
     * @param userId
     * @param newBalance
     */
    public void updateBalance(long userId, BigDecimal newBalance){
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, userId);

    }
}
