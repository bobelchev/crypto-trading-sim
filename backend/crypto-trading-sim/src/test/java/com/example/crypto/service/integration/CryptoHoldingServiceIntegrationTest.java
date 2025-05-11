package com.example.crypto.service.integration;

import com.example.crypto.model.TransactionType;
import com.example.crypto.service.CryptoHoldingService;
import com.example.crypto.service.UserService;
import com.example.crypto.websocketclient.WebSocketConfig;
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
            INSERT INTO transactions (user_id, crypto_ticker, quantity, price, transaction_type, timestamp, profit_loss)
            VALUES 
            (1, 'BTC', 0.075423, 46231.128400, 'BUY', CURRENT_TIMESTAMP, 0.0),
            (1, 'ETH', 1.235670, 2745.326700, 'SELL', CURRENT_TIMESTAMP, 0.0);
        """);
        jdbcTemplate.execute("""
            INSERT INTO holdings (user_id, crypto_ticker, quantity, average_price)
            VALUES 
            (1, 'BTC', 0.075423,27000.50),
            (1, 'ETH', 1.235670, 1850.00);
            """);
    }
    @Test
    public void testNewTicker(){
        //new ticker would be something different that BTC and ETH
        String newTicker = "XRP";
        BigDecimal quantity = new BigDecimal("10.000000");
        BigDecimal price = new BigDecimal("2.4");

        String sql = "SELECT COUNT(*) FROM holdings WHERE user_id = ? AND crypto_ticker = ?";

        Integer preInsertCount = jdbcTemplate.queryForObject(
                sql,
                Integer.class, USERID, newTicker
        );
        assertEquals(0, preInsertCount);

        cryptoHoldingService.handleHolding(USERID,newTicker,quantity, TransactionType.BUY,price);

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
        BigDecimal price = new BigDecimal("30000");

        String quantitySql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";
        String avgPriceSql = "SELECT average_price FROM holdings WHERE user_id = ? AND crypto_ticker = ?";


        BigDecimal oldQuantity = jdbcTemplate.queryForObject(
                quantitySql,
                BigDecimal.class, USERID, ticker
        );
        BigDecimal oldAvgPrice = jdbcTemplate.queryForObject(
                avgPriceSql,
                BigDecimal.class, USERID, ticker
        );

        assertEquals(new BigDecimal("0.075423"), oldQuantity);
        assertEquals(new BigDecimal("27000.500000"), oldAvgPrice);


        cryptoHoldingService.handleHolding(USERID,ticker,quantity, TransactionType.BUY,price);

        BigDecimal newQuantity = jdbcTemplate.queryForObject(
                quantitySql,
                BigDecimal.class, USERID, ticker
        );

        assertEquals(new BigDecimal("0.075423").add(quantity), newQuantity);

        BigDecimal expectedAvgPrice = oldAvgPrice.multiply(oldQuantity)
                .add(price.multiply(quantity))
                .divide(newQuantity, 6);
        BigDecimal storedAvgPrice = jdbcTemplate.queryForObject(avgPriceSql, BigDecimal.class, USERID, ticker);

    }
}
