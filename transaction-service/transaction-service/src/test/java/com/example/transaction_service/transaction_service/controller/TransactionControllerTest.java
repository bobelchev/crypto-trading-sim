package com.example.transaction_service.transaction_service.controller;


import com.example.transaction_service.transaction_service.controller.dto.TransactionDTO;
import com.example.transaction_service.transaction_service.model.TransactionType;
import com.example.transaction_service.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.transaction_service.service.TransactionService;
import com.example.transaction_service.transaction_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
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
    private static final String AUTH_HEADER = "Bearer mock-jwt-token";


    @Test
    public void testMakeTransaction() throws Exception {
        String jsonRequest = """
            {
                "cryptoTicker": "ETH",
                "quantity": 1.500000,
                "price": 2000.000000,
                "type": "BUY"
            }
        """;
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.getIdFromToken("mock-jwt-token"))
                    .thenReturn("1");
            mockMvc.perform(post("/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", AUTH_HEADER)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Transaction successful."));

            // Optional: use ArgumentCaptor for more flexibility
            verify(transactionService).makeTx(any(TransactionDTO.class), eq(AUTH_HEADER));
        }

    }
}
