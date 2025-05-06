package com.example.crypto.service;
import com.example.crypto.repository.CryptoHoldingRepository;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class UserServiceTest {

    UserRepository mockUserRepository;
    TransactionRepository mockTxRepository;
    CryptoHoldingService mockHoldingService;
    UserService userService;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    @BeforeEach
    void setUp(){
        mockUserRepository = mock(UserRepository.class);
        mockTxRepository = mock(TransactionRepository.class);
        mockHoldingService = mock(CryptoHoldingService.class);

        userService = new UserService();
        userService.userRepository = mockUserRepository;
        userService.transactionRepository = mockTxRepository;
        userService.cryptoHoldingService = mockHoldingService;
    }

    @Test
    public void testGetBalance(){
        when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(new BigDecimal("10000.000000"));
        BigDecimal result = userService.getBalance(USERID);
        assertEquals(DEFAULT_BALANCE,result);
        verify(mockUserRepository, times(1)).getBalanceOfUser(USERID);
    }
    @Test
    public void testReset(){
        userService.resetAccount(USERID);
        verify(mockUserRepository, times(1)).resetBalance(USERID);
        verify(mockTxRepository,times(1)).deleteAllTxs(USERID);
        verify(mockHoldingService,times(1)).deleteAllHoldingsOfUser(USERID);
    }

}
