package com.example.transaction_service.transaction_service.service;


import com.example.transaction_service.transaction_service.client.HoldingClient;
import com.example.transaction_service.transaction_service.client.UserClient;
import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.exception.InsufficientBalanceException;
import com.example.transaction_service.transaction_service.exception.InsufficientHoldingsException;
import com.example.transaction_service.transaction_service.exception.InvalidTransactionException;
import com.example.transaction_service.transaction_service.model.Transaction;
import com.example.transaction_service.transaction_service.model.TransactionType;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.validation.TransactionValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private static TransactionService transactionService;
    private static TransactionValidator mockValidator;
    private static UserClient mockUserClient;
    private static HoldingClient mockHoldingClient;
    private static TransactionRepository mockTxRepository;




    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");
    public static final long USERID = 1L;
    private static final String DEFAULT_TICKER = "BTC";
    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal("0.500000");
    private static final BigDecimal NEGATIVE_QUANTITY = new BigDecimal("-0.500000");
    private static final BigDecimal DEFAULT_PRICE = new BigDecimal("100.000000");
    //200*100=20 000>10 000
    private static final BigDecimal EXCESSIVE_QUANTITY = new BigDecimal("200.000000");
    private static final TransactionType BUY = TransactionType.BUY;
    private static final TransactionType SELL = TransactionType.SELL;

    @BeforeAll
    static void setUp(){
        mockValidator = mock(TransactionValidator.class);
        mockTxRepository = mock(TransactionRepository.class);
        mockUserClient = mock(UserClient.class);
        mockHoldingClient = mock(HoldingClient.class);
        transactionService = new TransactionService(
                mockTxRepository,
                mockValidator,
                mockUserClient,
                mockHoldingClient
        );
    }
    @BeforeEach
    void resetMocks() {
        reset(mockTxRepository, mockUserClient,mockHoldingClient);
    }
    @Test
    public void testBuy(){
        BigDecimal cost = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE);
        doNothing().when(mockValidator).validate(any(), any(), any());
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(DEFAULT_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(BUY);
        transactionService.makeTx(dto, "Bearer fake-token");
        //verify(mockUserRepository).updateBalance(USERID,DEFAULT_BALANCE.subtract(cost));
        //verify(mockHoldingService).handleHolding(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY, BUY, DEFAULT_PRICE);
        verify(mockUserClient).getUserBalance(anyString());
        verify(mockUserClient).updateBalance(USERID, DEFAULT_BALANCE.subtract(cost));

        //not testing timestamp or profit loss (we have to mock quite a lot from holding service)
        //verify(mockTxRepository,times(1)).insertTx(new Transaction(USERID,DEFAULT_TICKER, DEFAULT_QUANTITY, DEFAULT_PRICE, any(LocalDateTime.class), BUY, any(BigDecimal.class)));
    }
    @Test
    public void testSell(){
        BigDecimal cost = DEFAULT_QUANTITY.multiply(DEFAULT_PRICE);
        doNothing().when(mockValidator).validate(any(), any(), any());
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingClient.getAveragePrice(USERID, DEFAULT_TICKER))
                .thenReturn(new BigDecimal("80.000000"));
        when(mockHoldingClient.getTickerQuantity(USERID, DEFAULT_TICKER))
                .thenReturn(new BigDecimal("1.000000"));
        //when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));
        //when(mockHoldingService.getAveragePrice(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("80.000000"));
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(DEFAULT_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(SELL);
        transactionService.makeTx(dto, "Bearer fake-token");



        //verify(mockUserRepository).updateBalance(USERID,DEFAULT_BALANCE.add(cost));
        //verify(mockHoldingService).handleHolding(USERID,DEFAULT_TICKER,DEFAULT_QUANTITY,SELL,DEFAULT_PRICE);
        verify(mockUserClient).getUserBalance(anyString());
        verify(mockUserClient).updateBalance(USERID, DEFAULT_BALANCE.add(cost));
        // This line is commented out because LocalDateTime.now() produces a different timestamp each time it's called,
        //TODO: solve the timestamp problem
        //verify(mockTxRepository,times(1)).insertTx(new Transaction(USERID,DEFAULT_TICKER, DEFAULT_QUANTITY, DEFAULT_PRICE, any(LocalDateTime.class), SELL, any(BigDecimal.class)));

    }
    @Test
    public void testIllegalSellNegativeQuantity(){
        BigDecimal cost = NEGATIVE_QUANTITY.multiply(DEFAULT_PRICE);
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingClient.getTickerQuantity(USERID, DEFAULT_TICKER))
                .thenReturn(new BigDecimal("1.000000"));
        //when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        //when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(NEGATIVE_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(SELL);
        doThrow(new InvalidTransactionException("Quantity must be a positive number."))
                .when(mockValidator).validate(dto, DEFAULT_BALANCE, new BigDecimal("1.000000"));
        Exception exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.makeTx(dto, "Bearer fake-token");

        });
        assert(exception.getMessage().contains("Quantity must be a positive number."));
        //THIS SHOULD GET CALLED IN ORDER FOR THE VALIDATION TO WORK
        verify(mockUserClient).getUserBalance(anyString());
        //verify(mockUserRepository,never()).updateBalance(anyLong(),any(BigDecimal.class));
        //verify(mockHoldingService,never()).handleHolding(anyLong(), anyString(), any(BigDecimal.class), any(TransactionType.class), any(BigDecimal.class));
        verify(mockTxRepository, never()).insertTx(any(Transaction.class));
        verify(mockUserClient,never()).updateBalance(anyLong(), any(BigDecimal.class));
    }
    @Test
    public void testIllegalBuyNegativeQuantity(){
        BigDecimal cost = NEGATIVE_QUANTITY.multiply(DEFAULT_PRICE);
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingClient.getTickerQuantity(USERID, DEFAULT_TICKER))
                .thenReturn(new BigDecimal("1.000000"));
        //when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        //when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(NEGATIVE_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(BUY);
        doThrow(new InvalidTransactionException("Quantity must be a positive number."))
                .when(mockValidator).validate(dto, DEFAULT_BALANCE, new BigDecimal("1.000000"));
        Exception exception = assertThrows(InvalidTransactionException.class, () -> {
            transactionService.makeTx(dto, "Bearer fake-token");

        });
        assert(exception.getMessage().contains("Quantity must be a positive number."));
        //THIS SHOULD GET CALLED IN ORDER FOR THE VALIDATION TO WORK
        verify(mockUserClient).getUserBalance(anyString());

        //verify(mockUserRepository,never()).updateBalance(anyLong(),any(BigDecimal.class));
        //verify(mockHoldingService,never()).handleHolding(anyLong(), anyString(), any(BigDecimal.class), any(TransactionType.class), any(BigDecimal.class));
        verify(mockTxRepository, never()).insertTx(any(Transaction.class));
        verify(mockUserClient,never()).updateBalance(anyLong(), any(BigDecimal.class));
    }
    @Test
    public void testIllegalBuyExcessiveQuantity(){
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingClient.getTickerQuantity(USERID, DEFAULT_TICKER))
                .thenReturn(new BigDecimal("1.000000"));
        //when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        //when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(new BigDecimal("1.000000"));
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(EXCESSIVE_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(BUY);
        doThrow(new InsufficientBalanceException("Insufficient balance to complete the purchase."))
                .when(mockValidator).validate(dto, DEFAULT_BALANCE, new BigDecimal("1.000000"));
       Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
           transactionService.makeTx(dto, "Bearer fake-token");
       });
        assert(exception.getMessage().contains("Insufficient balance to complete the purchase."));
        //THIS SHOULD GET CALLED IN ORDER FOR THE VALIDATION TO WORK
        verify(mockUserClient).getUserBalance(anyString());

        //verify(mockUserRepository,never()).updateBalance(anyLong(),any(BigDecimal.class));
        //verify(mockHoldingService,never()).handleHolding(anyLong(), anyString(), any(BigDecimal.class), any(TransactionType.class), any(BigDecimal.class));
        verify(mockTxRepository, never()).insertTx(any(Transaction.class));
        verify(mockUserClient,never()).updateBalance(anyLong(), any(BigDecimal.class));
    }
    @Test
    public void testIllegalSellExcessiveQuantity(){
        BigDecimal currentHoldings = new BigDecimal("0.1");
        BigDecimal cost = EXCESSIVE_QUANTITY.multiply(DEFAULT_PRICE);
        when(mockUserClient.getUserBalance(anyString())).thenReturn(DEFAULT_BALANCE);
        when(mockHoldingClient.getTickerQuantity(USERID, DEFAULT_TICKER))
                .thenReturn(currentHoldings);
        //when(mockUserRepository.getBalanceOfUser(USERID)).thenReturn(DEFAULT_BALANCE);
        //when(mockHoldingService.getTickerQuantity(USERID, DEFAULT_TICKER)).thenReturn(currentHoldings);
        TransactionDTO dto = new TransactionDTO();
        dto.setUserId(USERID);
        dto.setCryptoTicker(DEFAULT_TICKER);
        dto.setQuantity(EXCESSIVE_QUANTITY);
        dto.setPrice(DEFAULT_PRICE);
        dto.setType(SELL);
        doThrow(new InsufficientHoldingsException("Insufficient holdings to complete the sale."))
                .when(mockValidator).validate(dto, DEFAULT_BALANCE, currentHoldings);
        Exception exception = assertThrows(InsufficientHoldingsException.class, () -> {
            transactionService.makeTx(dto, "Bearer fake-token");

        });
        assert(exception.getMessage().contains("Insufficient holdings to complete the sale."));
        //THIS SHOULD GET CALLED IN ORDER FOR THE VALIDATION TO WORK
        verify(mockUserClient).getUserBalance(anyString());

        //verify(mockUserRepository,never()).updateBalance(anyLong(),any(BigDecimal.class));
        //verify(mockHoldingService,never()).handleHolding(anyLong(), anyString(), any(BigDecimal.class), any(TransactionType.class), any(BigDecimal.class));
        verify(mockTxRepository, never()).insertTx(any());
        verify(mockUserClient,never()).updateBalance(anyLong(), any(BigDecimal.class));
    }

}
