package com.example.crypto.repository;

import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TransactionRepository.class)
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    public void setUp(){
        //clean the table because otherwise it will be new id for each test
        jdbcTemplate.execute("DELETE FROM transactions");
        //id did not reset properly
        jdbcTemplate.execute("ALTER TABLE transactions ALTER COLUMN id RESTART WITH 1");


        jdbcTemplate.execute("""
            INSERT INTO transactions (user_id, crypto_ticker, quantity, price, transaction_type, timestamp)
            VALUES 
            (1, 'BTC', 0.075423, 46231.128400, 'BUY', CURRENT_TIMESTAMP),
            (1, 'ETH', 1.235670, 2745.326700, 'SELL', CURRENT_TIMESTAMP);
        """);
    }

    /**
     * Tests if it correctly returns a transaction
     */
    @Test
    public void testGetSingleTx(){
        //that should return the btc buy tx
        Transaction transaction = transactionRepository.getSingleTx(1L);
        assertNotNull(transaction);
        assertEquals(1L,transaction.getUserId());
        assertEquals("BTC",transaction.getCryptoTicker());
        assertEquals(new BigDecimal("0.075423"), transaction.getQuantity());
        assertEquals(new BigDecimal("46231.128400"), transaction.getPrice());
        assertEquals(TransactionType.BUY, transaction.getTransactionType());
    }

    /**
     * Tests if correctly returns all transactions
     */
    @Test
    public void testGetAllTx(){
        List<Transaction> transactions = transactionRepository.getAllTxForUser(1L);
        assertEquals(2, transactions.size());
        Transaction firstTx = transactions.get(0);
        assertEquals(1L,firstTx.getUserId());
        assertEquals("BTC",firstTx.getCryptoTicker());
        assertEquals(new BigDecimal("0.075423"), firstTx.getQuantity());
        assertEquals(new BigDecimal("46231.128400"), firstTx.getPrice());
        assertEquals(TransactionType.BUY, firstTx.getTransactionType());
        Transaction secondTx = transactions.get(1);
        assertEquals(1L,secondTx.getUserId());
        assertEquals("ETH",secondTx.getCryptoTicker());
        assertEquals(new BigDecimal("1.235670"), secondTx.getQuantity());
        assertEquals(new BigDecimal("2745.326700"), secondTx.getPrice());
        assertEquals(TransactionType.SELL, secondTx.getTransactionType());
        //test with 0 txs in db
        jdbcTemplate.execute("DELETE FROM transactions");
        transactions = transactionRepository.getAllTxForUser(1L);
        assertEquals(0, transactions.size());
    }

    /**
     * Tests if new transaction gets inserted
     */
    @Test
    public void testInsertingTx(){
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND crypto_ticker = ?",
                Integer.class,
                1L, "XRP"
        );
        //should be 0
        assertEquals(0,count);

        //insert tx
        Transaction newTx = new Transaction(
                1L,
                "XRP",
                new BigDecimal("2.004500"),
                new BigDecimal("1.887650"),
                LocalDateTime.now(),
                TransactionType.BUY
        );
        transactionRepository.insertTx(newTx);
        //querying again should return 1
        count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND crypto_ticker = ?",
                Integer.class,
                1L, "XRP"
        );
        //should be 0
        assertEquals(1,count);
        count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transactions",
                Integer.class
        );
        assertEquals(3,count);
    }
}
