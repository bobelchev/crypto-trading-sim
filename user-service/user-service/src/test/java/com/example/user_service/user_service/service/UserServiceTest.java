package com.example.user_service.user_service.service;


import com.example.user_service.user_service.client.HoldingClient;
import com.example.user_service.user_service.client.TransactionClient;
import com.example.user_service.user_service.client.UserClient;
import com.example.user_service.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserRepository mockUserRepository;
    HoldingClient mockHoldingClient;
    TransactionClient mockTransactionClient;
    UserClient mockUserClient;
    UserService userService;
    PasswordEncoder passwordEncoder;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    @BeforeEach
    void setUp(){
        mockUserRepository = mock(UserRepository.class);
        mockHoldingClient = mock(HoldingClient.class);
        mockTransactionClient = mock(TransactionClient.class);
        mockUserClient = mock(UserClient.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserService(
                mockUserRepository,
                mockHoldingClient,
                mockTransactionClient,
                mockUserClient,
                passwordEncoder
        );
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
        //verify(mockUserClient).resetUser(USERID);
        verify(mockTransactionClient).deleteAllUserTransactions(USERID);
        verify(mockHoldingClient).deleteAllUserHoldings(USERID);
        //verify here if calls are made
        //verify(mockTxRepository,times(1)).deleteAllTxs(USERID);
        //verify(mockHoldingService,times(1)).deleteAllHoldingsOfUser(USERID);
    }
    @Test
    public void testUpdateBalance() {
        BigDecimal newBalance = new BigDecimal("5000.000000");
        userService.updateBalance(USERID, newBalance);

        verify(mockUserRepository, times(1)).updateBalance(USERID, newBalance);
    }


}
