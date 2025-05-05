package com.example.crypto.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//https://www.baeldung.com/spring-jdbctemplate-testing#h2db
//https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jdbc-test
@JdbcTest
@Import(UserRepository.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    /**
     * Initial balance should be 10 000 as per requirement
     */
    @Test
    public void testInitialBalance(){
        assertEquals(new BigDecimal("10000.000000"), userRepository.getBalanceOfUser(1L));
    }

    /**
     * Tests if balance gets updated in the DB
     */
    @Test
    public void testUpdatingBalance(){
        userRepository.updateBalance(1L, new BigDecimal("2000.000000"));
        assertEquals(new BigDecimal("2000.000000"),userRepository.getBalanceOfUser(1L));
    }

    /**
     * Tests if negative balance will be accepted
     */
    @Test
    public void testNegativeBalance(){
        BigDecimal oldBalance = userRepository.getBalanceOfUser(1L);
        userRepository.updateBalance(1L, new BigDecimal("-2000.000000"));
        BigDecimal newBalance = userRepository.getBalanceOfUser(1L);
        //there shouldn't be a change
        //additionally should throw an exception when implemented
        assertEquals(oldBalance, newBalance);

    }

}
