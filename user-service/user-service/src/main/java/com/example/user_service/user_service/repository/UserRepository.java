package com.example.user_service.user_service.repository;

import com.example.user_service.user_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

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
     * @param userId
     */
    public void resetBalance(long userId){
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql,DEFAULT_BALANCE,userId);
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try{
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setBalance(rs.getBigDecimal("balance"));
                return u;
            }, username);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
    public void addUser(String username, String email, String password){
        String sql = """
        INSERT INTO users (username,email,password, balance)
        VALUES (?, ?, ?,?)
        """;
        jdbcTemplate.update(sql, username, email, password, DEFAULT_BALANCE);


    }

}
