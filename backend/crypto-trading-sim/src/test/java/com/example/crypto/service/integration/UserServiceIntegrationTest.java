package com.example.crypto.service.integration;

import com.example.crypto.service.UserService;
import com.example.crypto.websocket.client.WebSocketConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ImportAutoConfiguration(exclude = WebSocketConfig.class)
@Transactional
public class UserServiceIntegrationTest {
    @Autowired
    UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final long USERID = 1L;
    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");

    @BeforeEach
    void setUp(){
        jdbcTemplate.execute("DELETE FROM transactions");
        jdbcTemplate.execute("DELETE FROM holdings");
        jdbcTemplate.execute("ALTER TABLE transactions ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE holdings ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("""
            INSERT INTO transactions (user_id, crypto_ticker, quantity, price, transaction_type, timestamp, profit_loss)
            VALUES 
            (1, 'BTC', 0.075423, 46231.128400, 'BUY', CURRENT_TIMESTAMP, 0.0),
            (1, 'ETH', 1.235670, 2745.326700, 'SELL', CURRENT_TIMESTAMP, 0.0);
        """);
        jdbcTemplate.execute("""
            INSERT INTO holdings (user_id, crypto_ticker, quantity, average_price)
            VALUES 
            (1, 'BTC', 0.075423, 27000.50),
            (1, 'ETH', 1.235670, 1850.00);
            """);

    }
    @Test
    public void testReset(){
        //update balance to different than default
        jdbcTemplate.update("UPDATE users SET balance = ? WHERE id = ?", new BigDecimal("50000.000000"), USERID);
        String sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal preResetBalance = jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class,
                USERID
        );
        //check if it is actually updated
        assertEquals(new BigDecimal("50000.000000"), preResetBalance);
        userService.resetAccount(USERID);
        BigDecimal resetBalance = jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class,
                USERID
        );
        assertEquals(DEFAULT_BALANCE, resetBalance);

        //transactions should be 0
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transactions WHERE user_id = ?",
                Integer.class,
                USERID
        );
        assertEquals(0, count);

        //holdings should be 0
        count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holdings WHERE user_id = ?",
                Integer.class,
                USERID
        );
        assertEquals(0, count);
    }
}
