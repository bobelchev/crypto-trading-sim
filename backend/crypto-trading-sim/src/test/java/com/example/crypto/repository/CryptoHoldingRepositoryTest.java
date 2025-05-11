package com.example.crypto.repository;

import com.example.crypto.model.CryptoHolding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(CryptoHoldingRepository.class)
public class CryptoHoldingRepositoryTest {
    @Autowired
    CryptoHoldingRepository cryptoHoldingRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static final long USERID = 1L;


    @BeforeEach
    public void setUp(){
        //clean the table because otherwise it will be new id for each test
        jdbcTemplate.execute("DELETE FROM holdings");
        //id did not reset properly
        jdbcTemplate.execute("ALTER TABLE holdings ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("""
            INSERT INTO holdings (user_id, crypto_ticker, quantity, average_price)
            VALUES 
            (1, 'BTC', 0.075423,27000.50),
            (1, 'ETH', 1.235670, 1850.00);
            """);
    }
    @Test
    public void testGetSingleHolding(){
        CryptoHolding btcHolding = cryptoHoldingRepository.getSingleHolding(1L);
        assertNotNull(btcHolding);
        assertEquals(1L,btcHolding.getId());
        assertEquals(USERID,btcHolding.getUserId());
        assertEquals("BTC",btcHolding.getCryptoTicker());
        assertEquals(new BigDecimal("0.075423"),btcHolding.getQuantity());
        assertEquals(new BigDecimal("27000.500000"), btcHolding.getAveragePrice());

    }
    @Test
    public void testGetAllHoldingOfUser(){
        List<CryptoHolding> allHoldings = cryptoHoldingRepository.getAllUserHoldings(1L);
        assertEquals(2, allHoldings.size());
        CryptoHolding firstHolding = allHoldings.get(0);
        assertEquals(USERID,firstHolding.getUserId());
        assertEquals("BTC",firstHolding.getCryptoTicker());
        assertEquals(new BigDecimal("0.075423"), firstHolding.getQuantity());
        assertEquals(new BigDecimal("27000.500000"), firstHolding.getAveragePrice());
        CryptoHolding secondHolding = allHoldings.get(1);
        assertEquals(USERID,secondHolding.getUserId());
        assertEquals("ETH",secondHolding.getCryptoTicker());
        assertEquals(new BigDecimal("1.235670"), secondHolding.getQuantity());
        assertEquals(new BigDecimal("1850.000000"), secondHolding.getAveragePrice());
        //test with 0 holdings in db
        jdbcTemplate.execute("DELETE FROM holdings");
        allHoldings = cryptoHoldingRepository.getAllUserHoldings(USERID);
        assertEquals(0, allHoldings.size());
    }
    @Test
    public void testInsertHolding(){
        CryptoHolding newHolding = new CryptoHolding(
                USERID,
                "XRP",
                new BigDecimal("4500.567812"),
                new BigDecimal("2.40")
        );
        cryptoHoldingRepository.insertHolding(newHolding);
        //using raw SQL instead of method to reduce dependency on other methods
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holdings WHERE user_id = ? AND crypto_ticker = ?",
                Integer.class,
                USERID, "XRP"
        );
        assertEquals(1, count);
        count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holdings WHERE user_id=?",
                Integer.class,
                USERID
        );
        //should be three holdings now BTC, ETH and the newly added XRP
        assertEquals(3, count);
    }

    /**
     * Tests the deletion of all holdings works correctly
     */
    @Test
    public void testDeleteAllHoldings(){
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holdings WHERE user_id=?",
                Integer.class,
                USERID
        );
        assertEquals(2, count);
        cryptoHoldingRepository.deleteHoldings(USERID);
         count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM holdings WHERE user_id=?",
                Integer.class,
                 USERID
        );
        assertEquals(0, count);



    }
}
