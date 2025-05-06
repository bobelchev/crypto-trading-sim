package com.example.crypto.controller;

import com.example.crypto.model.TransactionType;
import com.example.crypto.service.TransactionService;
import com.example.crypto.service.UserService;
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
                1L,
                "ETH",
                new BigDecimal("1.500000"),
                new BigDecimal("2000.000000"),
                TransactionType.BUY
        );

    }
}
