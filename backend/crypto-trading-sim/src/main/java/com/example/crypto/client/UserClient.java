package com.example.crypto.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/users/balance")
    BigDecimal getUserBalance(@RequestParam("userId") long userId);
    @PostMapping("/users/updateBalance")
    void updateBalance(@RequestParam("userId") long userId,@RequestParam("newBalance") BigDecimal newBalance);

}
