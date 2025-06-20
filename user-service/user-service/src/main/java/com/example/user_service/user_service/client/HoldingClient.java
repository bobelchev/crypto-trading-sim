package com.example.user_service.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "holding-service",contextId = "holdingClient")
public interface HoldingClient {
    @DeleteMapping("/holdings/{userId}")
    void deleteAllUserHoldings(@PathVariable("userId") long userId);
}