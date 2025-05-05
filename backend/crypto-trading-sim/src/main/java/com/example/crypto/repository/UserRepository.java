package com.example.crypto.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");

    /**
     * Returns the balance of a User according to its id
     * TODO: implement no such user found logic
     */
    public BigDecimal getBalanceOfUser(long userId){
        //on purpose so that test fails for now
        String sql = "SELECT balance FROM users WHERE id=?";
        return jdbcTemplate.queryForObject(sql,BigDecimal.class, userId);
    }

    /**
     * Updates the balance of the given user in the DB
     * TODO:implement defensive programming for negative balance
     * @param userId
     * @param newBalance
     */
    public void updateBalance(long userId, BigDecimal newBalance){
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, userId);
    }

    /**
     * Resets the balance of a user to the default
     * TODO: rethink at a late point
     * @param userId
     */
    public void resetBalance(long userId){
        String sql = "UPDATE user SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql,DEFAULT_BALANCE,userId);
    }
}
