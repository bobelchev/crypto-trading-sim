package com.example.transaction_service.transaction_service.controller;


import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.model.TransactionType;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;
    @MockitoBean
    private TransactionRepository transactionRepository;
    public static final long USERID = 1L;
    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");

    @Test
    public void testMakeTransaction() throws Exception {
        String jsonRequest = """
            {
                "userId": 1,
                "cryptoTicker": "ETH",
                "quantity": 1.500000,
                "price": 2000.000000,
                "type": "BUY"
            }
        """;
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction successful."));
        verify(transactionService).makeTx(
                new TransactionDTO() {{
                    setUserId(1L);
                    setCryptoTicker("ETH");
                    setQuantity(new BigDecimal("1.500000"));
                    setPrice(new BigDecimal("2000.000000"));
                    setType(TransactionType.BUY);
                }}
        );

    }
}
