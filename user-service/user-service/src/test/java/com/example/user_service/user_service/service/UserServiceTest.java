package com.example.user_service.user_service.service;


import com.example.user_service.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    UserRepository mockUserRepository;
    UserService userService;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    @BeforeEach
    void setUp(){
        mockUserRepository = mock(UserRepository.class);
        userService = new UserService();
        userService.userRepository = mockUserRepository;
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
