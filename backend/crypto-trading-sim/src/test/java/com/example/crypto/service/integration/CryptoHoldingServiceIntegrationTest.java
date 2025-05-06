package com.example.crypto.service.integration;

import com.example.crypto.model.TransactionType;
import com.example.crypto.service.CryptoHoldingService;
import com.example.crypto.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CryptoHoldingServiceIntegrationTest {
    @Autowired
    CryptoHoldingService cryptoHoldingService;
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
            INSERT INTO transactions (user_id, crypto_ticker, quantity, price, transaction_type, timestamp)
            VALUES 
            (1, 'BTC', 0.075423, 46231.128400, 'BUY', CURRENT_TIMESTAMP),
            (1, 'ETH', 1.235670, 2745.326700, 'SELL', CURRENT_TIMESTAMP);
        """);
        jdbcTemplate.execute("""
            INSERT INTO holdings (user_id, crypto_ticker, quantity)
            VALUES 
            (1, 'BTC', 0.075423),
            (1, 'ETH', 1.235670);
            """);
    }
    @Test
    public void testNewTicker(){
        //new ticker would be something different that BTC and ETH
        String newTicker = "XRP";
        BigDecimal quantity = new BigDecimal("10.000000");
        String sql = "SELECT COUNT(*) FROM holdings WHERE user_id = ? AND crypto_ticker = ?";

        Integer preInsertCount = jdbcTemplate.queryForObject(
                sql,
                Integer.class, USERID, newTicker
        );
        assertEquals(0, preInsertCount);

        cryptoHoldingService.handleHolding(USERID,newTicker,quantity, TransactionType.BUY);

        Integer postInsertCount = jdbcTemplate.queryForObject(
                sql,
                Integer.class, USERID, newTicker
        );
        assertEquals(1, postInsertCount);
    }
    @Test
    public void testExistingTicker(){
        //BTC
        String ticker = "BTC";
        BigDecimal quantity = new BigDecimal("10.000000");
        String sql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";

        BigDecimal oldQuantity = jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class, USERID, ticker
        );
        assertEquals(new BigDecimal("0.075423"), oldQuantity);

        cryptoHoldingService.handleHolding(USERID,ticker,quantity, TransactionType.BUY);

        BigDecimal newQuantity = jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class, USERID, ticker
        );
        assertEquals(new BigDecimal("0.075423").add(quantity), newQuantity);
    }
}
