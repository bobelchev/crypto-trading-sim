package com.example.crypto.controller;

import com.example.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/balance")
    public BigDecimal getUserBalance(@RequestParam long userId) {
        return new BigDecimal("10000.000000");
    }
    @PostMapping("/reset")
    public void resetUserAccount(@RequestParam long userId) {

    }
}
