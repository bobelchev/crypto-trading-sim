package com.example.crypto.service;

import com.example.crypto.model.TransactionType;
import com.example.crypto.repository.TransactionRepository;
import com.example.crypto.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    UserRepository mockUserRepository;
    TransactionRepository mockTxRepository;
    CryptoHoldingService mockHoldingService;
    TransactionService transactionService;

    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    private static final String DEFAULT_TICKER = "BTC";
    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal("0.500000");
    private static final BigDecimal NEGATIVE_QUANTITY = new BigDecimal("-0.500000");
    private static final BigDecimal DEFAULT_PRICE = new BigDecimal("100.000000");
    private static final TransactionType BUY = TransactionType.BUY;
    private static final TransactionType SELL = TransactionType.SELL;

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
        BigDecimal cost = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE);
        //added this one because did not anticipate to be needed before
        when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        transactionService.makeTx(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY,DEFAULT_PRICE,BUY);
        verify(mockUserRepository,times(1)).updateBalance(USERID,DEFAULT_BALANCE.subtract(cost));
        verify(mockHoldingService,times(1)).handleHolding(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY, BUY);
        // This line is commented out because LocalDateTime.now() produces a different timestamp each time it's called,
        //TODO: solve the timestamp problem

        //verify(mockTxRepository,times(1)).insertTx(new Transaction(USERID,cryptoTicker, quantity, price, LocalDateTime.now(), type));
    }
    @Test
    public void testSell(){
        BigDecimal cost = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE);

        //added this one because did not anticipate to be needed before
        when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));

        transactionService. makeTx(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY,DEFAULT_PRICE,SELL);
        verify(mockUserRepository,times(1)).updateBalance(USERID,DEFAULT_BALANCE.add(cost));
        verify(mockHoldingService,times(1)).handleHolding(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY,SELL);
        // This line is commented out because LocalDateTime.now() produces a different timestamp each time it's called,
        //TODO: solve the timestamp problem
        //verify(mockTxRepository,times(1)).insertTx(new Transaction(USERID,cryptoTicker, quantity, price, LocalDateTime.now(), type));
    }
    @Test
    public void testIllegalSellNegativeQuantity(){
        BigDecimal cost = NEGATIVE_QUANTITY.multiply(DEFAULT_PRICE);

        //added this one because did not anticipate to be needed before
        when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.makeTx(USERID, DEFAULT_TICKER, NEGATIVE_QUANTITY, DEFAULT_PRICE, SELL);
        });
        assert(exception.getMessage().contains("Quantity must be a positive number."));
        verify(mockUserRepository,times(0)).updateBalance(USERID,DEFAULT_BALANCE.add(cost));
        verify(mockHoldingService,times(0)).handleHolding(USERID,DEFAULT_TICKER, NEGATIVE_QUANTITY,SELL);
        verify(mockTxRepository, never()).insertTx(any());
    }
    @Test
    public void testIllegalBuyNegativeQuantity(){
        BigDecimal cost = NEGATIVE_QUANTITY.multiply(DEFAULT_PRICE);
        //added this one because did not anticipate to be needed before
        when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactionService.makeTx(USERID, DEFAULT_TICKER, NEGATIVE_QUANTITY, DEFAULT_PRICE, BUY);
        });
        assert(exception.getMessage().contains("Quantity must be a positive number."));
        verify(mockUserRepository,times(0)).updateBalance(USERID,DEFAULT_BALANCE.add(cost));
        verify(mockHoldingService,times(0)).handleHolding(USERID,DEFAULT_TICKER,NEGATIVE_QUANTITY,BUY);
        verify(mockTxRepository, never()).insertTx(any());
    }

}
