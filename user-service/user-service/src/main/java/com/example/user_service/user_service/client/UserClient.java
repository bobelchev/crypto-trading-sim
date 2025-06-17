package com.example.user_service.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="crypto-trading-sim",contextId = "userClient")
public interface UserClient {
    @PostMapping("/reset")
    void resetUser(@RequestParam("userId") long userId);
}
