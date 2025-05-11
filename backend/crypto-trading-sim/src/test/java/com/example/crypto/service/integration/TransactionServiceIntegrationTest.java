package com.example.crypto.service.integration;

import com.example.crypto.model.TransactionType;
import com.example.crypto.service.CryptoHoldingService;
import com.example.crypto.service.TransactionService;
import com.example.crypto.websocketclient.WebSocketConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ImportAutoConfiguration(exclude = WebSocketConfig.class)
@Transactional
public class TransactionServiceIntegrationTest {
    @Autowired
    TransactionService transactionService;
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
            (1, 'BTC', 0.075423, 46231.128400, 'BUY', CURRENT_TIMESTAMP,0.0),
            (1, 'ETH', 1.235670, 2745.326700, 'SELL', CURRENT_TIMESTAMP,0.0);
        """);
        jdbcTemplate.execute("""
            INSERT INTO holdings (user_id, crypto_ticker, quantity, average_price)
            VALUES 
            (1, 'BTC', 0.075423, 27000.50),
            (1, 'ETH', 1.235670, 1850.00);
            """);
    }
    /**
     * Test legal buy: when buyCost <= currentBalance
     */
    @Test
    public void testLegalBuy(){
        BigDecimal quantity = new BigDecimal("1.100000");
        BigDecimal price = new BigDecimal("2000.000000");
        BigDecimal cost = price.multiply(quantity);
        String ticker = "BTC";
        String sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal initialBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        //test if setup is ok
        assertEquals(DEFAULT_BALANCE, initialBalance);

        transactionService.makeTx(USERID, ticker, quantity, price, TransactionType.BUY);
        BigDecimal leftoverBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE.subtract(cost).setScale(6),leftoverBalance);

        //check if the holding is updated properly
        sql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";
        BigDecimal newHoldingQuantity = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID, ticker);
        BigDecimal calcNewQuantity = new BigDecimal("0.075423").add(quantity);
        assertEquals(calcNewQuantity, newHoldingQuantity);
    }

    /**
     * Test illegal buy: when buyCost > currentBalance
     */
    @Test
    public void testIllegalBuy(){
        BigDecimal quantity = new BigDecimal("1.100000");
        BigDecimal price = new BigDecimal("11000.000000");
        String ticker = "BTC";
        String sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal initialBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        //test if setup is ok
        //for this test default should be 10k to throw an exception
        assertEquals(DEFAULT_BALANCE, initialBalance);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.makeTx(USERID, ticker, quantity, price, TransactionType.BUY);
        });
        String expectedMessage = "Insufficient balance to complete the purchase.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        //check that nothing changed
        sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE, balance);

        sql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";
        BigDecimal sameQuantity = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID, ticker);
        //quantity of the BTC holding should not be changed
        assertEquals(new BigDecimal("0.075423"),sameQuantity);
    }

    /**
     * Test legal sell: when sellQuantity <= currentQuantity
     */
    @Test
    public void testLegalSell(){
        BigDecimal quantity = new BigDecimal("0.030000");
        BigDecimal price = new BigDecimal("1500.000000");
        BigDecimal cost = price.multiply(quantity);
        String ticker = "BTC";
        String sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal initialBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE, initialBalance);

        transactionService.makeTx(USERID, ticker, quantity, price, TransactionType.SELL);
        BigDecimal newBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE.add(cost).setScale(6),newBalance);

        //check if the holding is updated properly
        sql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";
        BigDecimal newHoldingQuantity = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID, ticker);
        BigDecimal calcNewQuantity = new BigDecimal("0.075423").subtract(quantity);
        assertEquals(calcNewQuantity, newHoldingQuantity);
        sql = "SELECT profit_loss FROM transactions WHERE user_id = ? AND crypto_ticker = ? ORDER BY id DESC LIMIT 1";
        BigDecimal profitOrLoss = jdbcTemplate.queryForObject(sql, BigDecimal.class, USERID, ticker);

        BigDecimal avgPrice = new BigDecimal("27000.50"); // from setup
        BigDecimal expectedProfit = price.subtract(avgPrice).multiply(quantity).setScale(6, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedProfit, profitOrLoss);
    }

    /**
     * Test illegal sell: when sellQuantity > holdingQuantity
     */
    @Test
    public void testIllegalSell(){
        BigDecimal quantity = new BigDecimal("1.000000");
        BigDecimal price = new BigDecimal("1500.000000");
        BigDecimal cost = price.multiply(quantity);
        String ticker = "BTC";
        String sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal initialBalance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE, initialBalance);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.makeTx(USERID, ticker, quantity, price, TransactionType.SELL);
        });
        String expectedMessage = "Insufficient holdings to complete the sale.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        //check that nothing changed
        sql = "SELECT balance FROM users WHERE id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID);
        assertEquals(DEFAULT_BALANCE, balance);

        sql = "SELECT quantity FROM holdings WHERE user_id = ? AND crypto_ticker = ?";
        BigDecimal sameQuantity = jdbcTemplate.queryForObject(
                sql, BigDecimal.class, USERID, ticker);
        //quantity of the BTC holding should not be changed
        assertEquals(new BigDecimal("0.075423"),sameQuantity);
    }

}
