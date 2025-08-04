package com.example.user_service.user_service.controller;

import com.example.user_service.user_service.model.User;
import com.example.user_service.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
       return userService.login(user.getUsername(), user.getPassword());
    }
    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.register(user.getUsername(), user.getEmail(),user.getPassword());
    }




}
