package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.CryptoHoldingRepository;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    UserRepository mockUserRepository;
    TransactionRepository mockTxRepository;
    CryptoHoldingService mockHoldingService;
    TransactionService transactionService;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    @BeforeEach
    void setUp(){
        mockUserRepository = mock(UserRepository.class);
        mockTxRepository = mock(TransactionRepository.class);
        mockHoldingService = mock(CryptoHoldingService.class);

        transactionService = new TransactionService();
        transactionService.userRepository = mockUserRepository;
        transactionService.transactionRepository = mockTxRepository;
        transactionService.cryptoHoldingService = mockHoldingService;
    }
    @Test
    public void testBuy(){
        BigDecimal quantity = new BigDecimal("0.5");
        String cryptoTicker = "BTC";
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal cost = quantity.multiply(price);
        transactionService.buy(USERID,cryptoTicker,quantity,price);
        verify(mockUserRepository,times(1)).updateBalance(USERID,DEFAULT_BALANCE.subtract(cost));
        verify(mockHoldingService,times(1)).handleHolding(USERID,cryptoTicker,quantity);
        verify(mockTxRepository,times(1))
                .insertTx(new Transaction(USERID,cryptoTicker, quantity, price, LocalDateTime.now(), TransactionType.BUY));
    }
    @Test
    public void testSell(){
        BigDecimal quantity = new BigDecimal("0.5");
        String cryptoTicker = "BTC";
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal cost = quantity.multiply(price);
        transactionService.sell(USERID,cryptoTicker,quantity,price);
        verify(mockUserRepository,times(1)).updateBalance(USERID,DEFAULT_BALANCE.add(cost));
        verify(mockHoldingService,times(1)).handleHolding(USERID,cryptoTicker,quantity);
        verify(mockTxRepository,times(1))
                .insertTx(new Transaction(
                        USERID,cryptoTicker, quantity, price,
                        LocalDateTime.now(), TransactionType.SELL));
    }

}
