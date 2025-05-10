package com.example.crypto.service;

import com.example.crypto.model.CryptoHolding;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.CryptoHoldingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class CryptoHoldingServiceTest {
    private static CryptoHoldingRepository mockRepository;
    private static CryptoHoldingService holdingService;
    public static final long USERID = 1L;


    @BeforeAll
    static void setUp() {
        mockRepository = mock(CryptoHoldingRepository.class);
        holdingService = new CryptoHoldingService();
        holdingService.cryptoHoldingRepository = mockRepository;
    }
    @BeforeEach
    void resetMocks() {
        reset(mockRepository);
    }

    @Test
    public void testHandleHoldingWhenNotExist() {
        String ticker = "BTC";
        BigDecimal quantity = new BigDecimal("0.511012");

        when(mockRepository.getSingleHoldingByTickerAndUserId(USERID, ticker)).
                thenReturn(null);

        holdingService.handleHolding(USERID, ticker, quantity, TransactionType.BUY);
        verify(mockRepository, times(1)).
                insertHolding(new CryptoHolding(USERID, ticker, quantity));
    }
    @Test
    public void testHandleHoldingWhenExist() {
        String ticker = "BTC";
        BigDecimal quantity = new BigDecimal("0.511012");

        when(mockRepository.getSingleHoldingByTickerAndUserId(USERID, ticker)).
                thenReturn(new CryptoHolding(USERID,ticker,new BigDecimal("0.111111")));

        holdingService.handleHolding(USERID, ticker, quantity,TransactionType.BUY);

        verify(mockRepository, times(1)).
                updateHolding(new CryptoHolding(
                        USERID, ticker,
                        quantity.add(new BigDecimal("0.111111"))));
    }
}
