package com.example.crypto.controller;

import com.example.crypto.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    public static final long USERID = 1L;
    public static final BigDecimal DEFAULT_BALANCE = new BigDecimal("10000.000000");


    @Test
    public void testGetUserBalance() throws Exception {
        Mockito.when(userService.getBalance(USERID)).thenReturn(DEFAULT_BALANCE);
        mockMvc.perform(get("/users/balance")
                        .param("userId", String.valueOf(USERID)))
                .andExpect(status().isOk())
                .andExpect(content().string("10000.000000"));
    }
    @Test
    public void testResetUserAccount() throws Exception {
        mockMvc.perform(post("/users/reset")
                        .param("userId", String.valueOf(USERID)))
                .andExpect(status().isOk());

        verify(userService).resetAccount(USERID);
    }
}
