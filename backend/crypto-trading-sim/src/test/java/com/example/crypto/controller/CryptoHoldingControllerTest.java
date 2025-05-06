package com.example.crypto.controller;

import com.example.crypto.service.CryptoHoldingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(CryptoHoldingController.class)
public class CryptoHoldingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CryptoHoldingService holdingService;

    @Test
    public void testGetUserHoldingsWhenNone() throws Exception {
        long userId = 1L;

        //for now test for empty list
        when(holdingService.getHoldingsOfUser(userId)).thenReturn(List.of());

        mockMvc.perform(get("/holdings")
                        .param("userId", String.valueOf(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(holdingService).getHoldingsOfUser(userId);
    }

}
