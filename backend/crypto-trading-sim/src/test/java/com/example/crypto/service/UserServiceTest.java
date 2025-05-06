package com.example.crypto.service;
import com.example.crypto.repository.CryptoHoldingRepository;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class UserServiceTest {

    UserRepository mockUserRepository;
    TransactionService mockTxService;
    CryptoHoldingRepository mockHoldingRepository;
    UserService userService;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    @BeforeEach
    void setUp(){
        mockUserRepository = mock(UserRepository.class);
        mockTxService = mock(TransactionService.class);
        mockHoldingRepository = mock(CryptoHoldingRepository.class);

        userService = new UserService();
        userService.userRepository = mockUserRepository;
        userService.transactionService = mockTxService;
        userService.cryptoHoldingRepository = mockHoldingRepository;
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
        verify(mockTxService,times(1)).deleteAllTxsOfUser(USERID);
        verify(mockHoldingRepository,times(1)).deleteHoldings(USERID);
    }

}
